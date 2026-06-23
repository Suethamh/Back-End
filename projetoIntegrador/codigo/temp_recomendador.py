# =============================================================================
# *** SCRIPT TEMPORARIO / PROTOTIPO ***  (pode apagar depois)
# -----------------------------------------------------------------------------
# Recomendador por genero. Para cada (usuario, genero) usa 2 sinais:
#   GOSTO  = nota media do usuario no genero, CENTRALIZADA pela media dele
#            (positivo = gosta mais que a propria media; tira o vies de quem da
#             nota alta pra tudo)
#   VOLUME = log(1 + nº de filmes vistos no genero)  (o quanto CONSOME)
# GOSTO e VOLUME entram SEPARADOS -> um ALGORITMO GENETICO aprende um peso para
# cada um, por genero, e descobre sozinho quanto o volume conta mesmo com gosto
# baixo (caso "nao curte Acao mas assistiu muita").
#
# >>> METODOLOGIA (anti-vazamento + anti-overfit), 3 conjuntos DISJUNTOS:
#       PERFIL   -> constroi gosto/volume (so com essas avaliacoes)
#       VALIDACAO-> a fitness do GA e o AUC AQUI (held-out; o GA nao ve essas
#                   notas no perfil -> sem auto-inclusao, sem vazamento)
#       TESTE    -> numero final, honesto (nunca usado para escolher pesos)
#     Como os 3 sao disjuntos, a nota de um par NUNCA entra no perfil usado como
#     feature dele. A recomendacao final usa o perfil de TODAS as avaliacoes
#     (isso e o que se usaria "em producao"; nao ha rotulo em filme nao visto).
#
# ATENCAO: em base PEQUENA/SINTETICA (sem relacao real genero->nota) o AUC fica
#          ~0.5 mesmo correto - nao ha sinal. O valor aparece na base real.
# NAO faz parte do pipeline final; exploratorio.
# =============================================================================
import os
import numpy as np
import pandas as pd
from sklearn.metrics import roc_auc_score
from sklearn.model_selection import train_test_split

# --- Caminhos relativos a RAIZ do projeto (este script fica em codigo/) ------
RAIZ = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# --- Selecao do dataset (escolha a cada execucao) ---------------------------
print("Qual dataset usar?  1 = ml-latest-small (rapido)  |  2 = ml-latest (completa ~33M)")
_op = input("Digite 1 ou 2: ").strip()
BASE = "ml-latest" if _op == "2" else "ml-latest-small"
PASTA = os.path.join(RAIZ, BASE) + os.sep        # datasets ficam na RAIZ do projeto
print(f"Dataset escolhido: {PASTA}\n")

RANDOM_STATE  = 42
LIMIAR_GOSTOU = 4.0
TOP_N         = 10
LAMBDA_REG    = 0.02                 # regularizacao L2 leve (reduz overfit do GA)
POP, GER, ELITE, P_MUT = 40, 40, 4, 0.25

rng = np.random.RandomState(RANDOM_STATE)

# -----------------------------------------------------------------------------
# 1. Dados
# -----------------------------------------------------------------------------
filmes = (pd.read_csv(PASTA + "movies.csv", usecols=["movieId", "title", "genres"])
          .drop_duplicates("movieId"))
avaliacoes = pd.read_csv(PASTA + "ratings.csv", usecols=["userId", "movieId", "rating"])

# Na base COMPLETA (2), amostra usuarios para o prototipo nao estourar memoria
# (a base pequena (1) usa todos os usuarios).
N_AMOSTRA_USERS = 3000
if BASE == "ml-latest":
    uids = avaliacoes["userId"].unique()
    if len(uids) > N_AMOSTRA_USERS:
        sel = pd.Series(uids).sample(N_AMOSTRA_USERS, random_state=RANDOM_STATE)
        avaliacoes = avaliacoes[avaliacoes["userId"].isin(sel)]
        print(f"Base completa: amostrados {N_AMOSTRA_USERS} de {len(uids)} usuarios.")

filmes["genres_list"] = filmes["genres"].fillna("(no genres listed)").str.split("|")

fg = filmes[["movieId", "genres_list"]].explode("genres_list").reset_index(drop=True)
fg = fg[fg["genres_list"] != "(no genres listed)"]
generos = sorted(fg["genres_list"].unique())
G = len(generos)

filme_oh = pd.crosstab(fg["movieId"], fg["genres_list"])
filme_oh = (filme_oh > 0).astype(np.float32).reindex(
    index=filmes["movieId"].unique(), columns=generos, fill_value=0)
gen_por_filme = filme_oh.sum(axis=1)
filmes_com_genero = gen_por_filme.index[gen_por_filme > 0]

# -----------------------------------------------------------------------------
# 2. Funcoes de perfil (reutilizadas em PERFIL e em TODAS as avaliacoes)
# -----------------------------------------------------------------------------
def construir_perfil(df):
    """gosto (media centralizada) e volume (log contagem) por (usuario, genero)."""
    ag = df.merge(fg, on="movieId")
    g = ag.groupby(["userId", "genres_list"])["rating"]
    mean_ug = g.mean().unstack().reindex(columns=generos)
    count_ug = g.count().unstack(fill_value=0).reindex(columns=generos, fill_value=0)
    umean = df.groupby("userId")["rating"].mean()
    gosto = mean_ug.sub(umean, axis=0).fillna(0.0)
    volume = np.log1p(count_ug).fillna(0.0)
    return gosto, volume

def construir_qualidade(df):
    """Media bayesiana por filme (desempate da recomendacao)."""
    ms = df.groupby("movieId")["rating"].agg(n="count", media="mean")
    C = float(df["rating"].mean()); m0 = float(ms["n"].quantile(0.75))
    q = (ms["n"] * ms["media"] + m0 * C) / (ms["n"] + m0)
    return q.reindex(filme_oh.index).fillna(C)

def montar_P(user_ids, movie_ids, gosto, volume):
    """[GOSTO*onehot/ngen | VOLUME*onehot/ngen] para cada par (usuario, filme)."""
    Gu = gosto.reindex(user_ids).fillna(0.0).to_numpy()
    Vu = volume.reindex(user_ids).fillna(0.0).to_numpy()
    Mo = filme_oh.reindex(movie_ids).fillna(0.0).to_numpy()
    ng = Mo.sum(axis=1, keepdims=True); ng[ng == 0] = 1.0
    return np.hstack([Gu * Mo / ng, Vu * Mo / ng])

# -----------------------------------------------------------------------------
# 3. Pares rotulados + SPLIT em 3 conjuntos DISJUNTOS (perfil / validacao / teste)
# -----------------------------------------------------------------------------
pares = avaliacoes[avaliacoes["movieId"].isin(filme_oh.index)].copy()
pares["gostou"] = (pares["rating"] >= LIMIAR_GOSTOU).astype(np.int8)
y = pares["gostou"].to_numpy()

idx_perfil, idx_tmp = train_test_split(
    np.arange(len(pares)), test_size=0.50, random_state=RANDOM_STATE, stratify=y)
idx_val, idx_test = train_test_split(
    idx_tmp, test_size=0.50, random_state=RANDOM_STATE, stratify=y[idx_tmp])

# Perfil construido SO com o conjunto PERFIL (disjunto de val e teste)
gosto_p, volume_p = construir_perfil(pares.iloc[idx_perfil])

def P_de(idx):
    sub = pares.iloc[idx]
    return montar_P(sub["userId"].to_numpy(), sub["movieId"].to_numpy(), gosto_p, volume_p)

P_val, P_test = P_de(idx_val), P_de(idx_test)
y_val, y_test = y[idx_val], y[idx_test]
print(f"Generos: {G} | pesos: {2*G} | "
      f"perfil/val/teste: {len(idx_perfil)}/{len(idx_val)}/{len(idx_test)}")

# -----------------------------------------------------------------------------
# 4. ALGORITMO GENETICO: fitness = AUC na VALIDACAO (held-out) - regularizacao
# -----------------------------------------------------------------------------
def _auc(yy, s):
    # 0.5 se o score for constante OU se houver so uma classe (evita ValueError)
    if np.ptp(s) == 0 or len(np.unique(yy)) < 2:
        return 0.5
    return roc_auc_score(yy, s)

def fitness(w):
    return _auc(y_val, P_val @ w) - LAMBDA_REG * float(np.mean(w * w))   # penaliza pesos grandes

pop = rng.uniform(-1.0, 1.0, size=(POP, 2 * G))
best_w, best_fit = None, -1e9
for _ in range(GER):
    fits = np.array([fitness(w) for w in pop])
    if fits.max() > best_fit:
        best_fit = float(fits.max()); best_w = pop[int(fits.argmax())].copy()
    ordem = np.argsort(-fits); pop, fits = pop[ordem], fits[ordem]
    nova = [pop[i].copy() for i in range(ELITE)]
    while len(nova) < POP:
        i, j = rng.choice(POP, 2, replace=False); pai = pop[i] if fits[i] >= fits[j] else pop[j]
        k, l = rng.choice(POP, 2, replace=False); mae = pop[k] if fits[k] >= fits[l] else pop[l]
        filho = np.where(rng.rand(2 * G) < 0.5, pai, mae)
        if rng.rand() < P_MUT:
            filho = np.clip(filho + rng.normal(0, 0.1, 2 * G), -1.0, 1.0)
        nova.append(filho)
    pop = np.array(nova)

auc_val = _auc(y_val, P_val @ best_w)
auc_test = _auc(y_test, P_test @ best_w)
print(f"\nAUC validacao (usada p/ escolher pesos): {auc_val:.3f}")
print(f"AUC TESTE (honesto, conjunto nunca usado): {auc_test:.3f}")
if abs(auc_test - 0.5) < 0.02:
    print("  AVISO: AUC ~ 0.5 -> sem sinal aprendivel nesta base (ou base pequena/ruidosa);")
    print("         em base sintetica sem relacao genero->nota isso e o esperado.")

w_gosto = pd.Series(best_w[:G], index=generos)
w_vol = pd.Series(best_w[G:], index=generos)
print("\nPesos aprendidos por genero (+ recomenda mais | - recomenda menos):")
print(pd.DataFrame({"peso_gosto": w_gosto, "peso_volume": w_vol}).round(2).to_string())

# -----------------------------------------------------------------------------
# 5. RECOMENDACAO: perfil de TODAS as avaliacoes (uso "real"); empate por qualidade
# -----------------------------------------------------------------------------
gosto_all, volume_all = construir_perfil(pares)
qualidade = construir_qualidade(pares)
filmes_idx = filmes.set_index("movieId")

def recomendar(u, n=TOP_N):
    vistos = set(avaliacoes.loc[avaliacoes["userId"] == u, "movieId"])
    cand = np.array([m for m in filmes_com_genero if m not in vistos])
    score = montar_P(np.full(len(cand), u), cand, gosto_all, volume_all) @ best_w
    df = pd.DataFrame({"movieId": cand, "score": score,
                       "qualidade": qualidade.reindex(cand).to_numpy()})
    df = df.sort_values(["score", "qualidade"], ascending=False).head(n)   # qualidade = desempate
    df = df.merge(filmes_idx[["title", "genres"]], left_on="movieId", right_index=True)
    return df[["title", "genres", "score", "qualidade"]].round(3)

# Demo: 2 usuarios MAIS ativos + 1 POUCO ativo
ativ = avaliacoes["userId"].value_counts()
demo_users = list(dict.fromkeys(list(ativ.index[:2]) + [ativ.index[-1]]))   # sem duplicar
for u in demo_users:
    print("\n" + "=" * 70)
    print(f"USUARIO {u}  ({int(ativ.get(u, 0))} avaliacoes)")
    if u in gosto_all.index:
        g_top = gosto_all.loc[u].sort_values(ascending=False).head(3)
        v_top = volume_all.loc[u].sort_values(ascending=False).head(3)
        print("  Curte mais (acima da media dele): " +
              ", ".join(f"{g} ({val:+.2f})" for g, val in g_top.items()))
        print("  Assiste mais (volume): " +
              ", ".join(f"{g} ({int(round(np.expm1(val)))} filmes)" for g, val in v_top.items()))
    print(f"  TOP {TOP_N} recomendacoes:")
    print(recomendar(u).to_string(index=False))
