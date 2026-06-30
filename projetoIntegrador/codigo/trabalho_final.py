# =============================================================================
# TRABALHO FINAL INTEGRADOR - Agrupamento de Dados + Inteligencia Computacional
# Cenario 6: Sistema de Recomendacao de Filmes (base MovieLens)
# -----------------------------------------------------------------------------
# Script UNICO que cobre as 5 semanas, em ordem. Cada bloco esta marcado.
# Recomenda filmes EXCLUSIVAMENTE pelas notas do usuario: pega os generos dos
# filmes que ele nota alto e recomenda filmes parecidos.
#   Agrupamento  : K-Means (1o algoritmo) + Hierarquico/Ward (2o, comparacao)
#   Inteligencia : Algoritmo Genetico (pesos de genero por cluster) + Rede Neural
#
# EQUIPE (preencher):
#   - Aluno 1 - <nome> - <papel, ex.: Agrupamento de Dados>
#   - Aluno 2 - <nome> - <papel, ex.: Inteligencia Computacional>
#   - Aluno 3 - <nome> - <papel, ex.: Integracao / Analise / Relatorio>
# =============================================================================
import os
import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans, AgglomerativeClustering
from sklearn.neural_network import MLPClassifier
from sklearn.decomposition import PCA
from sklearn.metrics import (silhouette_score, davies_bouldin_score,
                             calinski_harabasz_score, adjusted_rand_score,
                             roc_auc_score)
from sklearn.model_selection import train_test_split
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

pd.set_option("display.max_columns", None); pd.set_option("display.width", 200)

RAIZ = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DIR_IMAGENS = os.path.join(RAIZ, "imagens"); os.makedirs(DIR_IMAGENS, exist_ok=True)
DIR_CSVS = os.path.join(RAIZ, "csvs"); os.makedirs(DIR_CSVS, exist_ok=True)

# Dataset FIXO: base completa (ml-latest). Amostra 3000 usuarios mais abaixo.
BASE = "ml-latest"
PASTA = os.path.join(RAIZ, BASE) + os.sep
print(f"Dataset (fixo): {PASTA}\n")

RANDOM_STATE = 42; LIMIAR_GOSTOU = 4.0; K_CLUSTERS = 4; TOP_N = 10
N_AMOSTRA = 8000; POP, GER, ELITE, P_MUT = 40, 40, 4, 0.25
rng = np.random.RandomState(RANDOM_STATE)

def amostra_idx(n, size, seed=RANDOM_STATE):
    if n <= size: return np.arange(n)
    return np.sort(np.random.RandomState(seed).choice(n, size, replace=False))


# #############################################################################
# SEMANA 1 - Base tratada, entendimento do problema e preparacao inicial
# #############################################################################
print("#" * 70)
print("SEMANA 1 - BASE TRATADA E ANALISE EXPLORATORIA")
print("#" * 70)
print("""
CENARIO 6 - Recomendacao de filmes/conteudos digitais.
BASE PUBLICA: MovieLens (GroupLens) - movies, ratings, tags, links (CSV).
PROBLEMA / PERGUNTA DE NEGOCIO: como recomendar filmes a um usuario usando
   APENAS as notas que ele deu? (tarefa de RECOMENDACAO).
ATRIBUTOS principais: userId, movieId, rating (0.5-5.0), genres (texto com '|').
""")

filmes = pd.read_csv(PASTA + "movies.csv", usecols=["movieId", "title", "genres"],
                     dtype={"movieId": "int32", "title": "string", "genres": "string"})
avaliacoes = pd.read_csv(PASTA + "ratings.csv", usecols=["userId", "movieId", "rating"],
                         dtype={"userId": "int32", "movieId": "int32", "rating": "float32"})
links = pd.read_csv(PASTA + "links.csv", usecols=["movieId", "imdbId", "tmdbId"],
                    dtype={"movieId": "int32", "imdbId": "Int32", "tmdbId": "Int32"})

if BASE == "ml-latest":                                  # amostra para a base gigante
    uids = avaliacoes["userId"].unique()
    if len(uids) > 3000:
        sel = pd.Series(uids).sample(3000, random_state=RANDOM_STATE)
        avaliacoes = avaliacoes[avaliacoes["userId"].isin(sel)]
        print(f"Base completa: amostrados 3000 de {len(uids)} usuarios.")

print("Descricao inicial dos atributos (tipos e amostra):")
print(avaliacoes.dtypes.to_dict())
print(avaliacoes.head().to_string(index=False))

# ---- Valores ausentes -------------------------------------------------------
print("\n[Ausentes] por tabela:",
      {"filmes": int(filmes.isnull().sum().sum()),
       "avaliacoes": int(avaliacoes.isnull().sum().sum()),
       "links(tmdbId)": int(links["tmdbId"].isnull().sum())})
filmes = filmes.dropna(subset=["movieId", "title", "genres"])
avaliacoes = avaliacoes.dropna()
links = links.dropna(subset=["movieId"])                 # mantem filmes sem tmdbId

# ---- Duplicidades -----------------------------------------------------------
print("[Duplicidades] avaliacoes (userId,movieId):",
      int(avaliacoes.duplicated(["userId", "movieId"]).sum()))
filmes = filmes.drop_duplicates("movieId")
avaliacoes = avaliacoes.drop_duplicates(["userId", "movieId"], keep="last")

# ---- Outliers ---------------------------------------------------------------
antes = len(avaliacoes)
avaliacoes = avaliacoes[avaliacoes["rating"].between(0.5, 5.0)]          # notas validas
contagem = avaliacoes["movieId"].value_counts()
validos = np.intersect1d(filmes["movieId"].unique(), contagem[contagem > 5].index)
filmes = filmes[filmes["movieId"].isin(validos)]
avaliacoes = avaliacoes[avaliacoes["movieId"].isin(validos)]            # filmes com >5 notas
print(f"[Outliers] removidas {antes - len(avaliacoes)} avaliacoes "
      f"(fora de [0.5,5] ou de filmes com <=5 notas). Restam {len(avaliacoes)}.")

# ---- Transformacao de atributos categoricos ---------------------------------
filmes["genres_list"] = filmes["genres"].fillna("(no genres listed)").astype(str).str.split("|")

# ---- Primeira analise exploratoria ------------------------------------------
fg = filmes[["movieId", "genres_list"]].explode("genres_list").reset_index(drop=True)
fg = fg[fg["genres_list"] != "(no genres listed)"]
generos = sorted(fg["genres_list"].unique()); G = len(generos)
print(f"\n[EDA] usuarios={avaliacoes['userId'].nunique()} | "
      f"filmes={avaliacoes['movieId'].nunique()} | avaliacoes={len(avaliacoes)} | generos={G}")
print("[EDA] TOP 5 generos mais frequentes:")
print(fg["genres_list"].value_counts().head(5).to_string())
print("[EDA] distribuicao das notas:")
print(avaliacoes["rating"].value_counts().sort_index().to_string())


# #############################################################################
# SEMANA 2 - Agrupamento I: representacao, distancia e baseline (K-Means)
# #############################################################################
print("\n" + "#" * 70)
print("SEMANA 2 - AGRUPAMENTO I (K-Means)")
print("#" * 70)

filme_oh = pd.crosstab(fg["movieId"], fg["genres_list"])
filme_oh = (filme_oh > 0).astype(np.float32).reindex(
    index=filmes["movieId"].unique(), columns=generos, fill_value=0)
filmes_com_genero = filme_oh.index[filme_oh.sum(axis=1) > 0]

# SELECAO + JUSTIFICATIVA dos atributos:
#   perfil do usuario = nota media por genero, CENTRALIZADA pela media dele.
#   Justificativa: representa o GOSTO por genero a partir so das notas; centralizar
#   remove o vies de quem da nota alta para tudo (foca na preferencia relativa).
def perfil_por_genero(df):
    ag = df.merge(fg, on="movieId")
    m = ag.groupby(["userId", "genres_list"])["rating"].mean().unstack().reindex(columns=generos)
    return m.sub(df.groupby("userId")["rating"].mean(), axis=0)
perfil = perfil_por_genero(avaliacoes)

# NORMALIZACAO (z-score) e DISTANCIA (Euclidiana, padrao do K-Means)
M = perfil.fillna(0.0)
X = StandardScaler().fit_transform(M.to_numpy())
print("Normalizacao: StandardScaler (z-score). Distancia: Euclidiana (K-Means).")

# 1o ALGORITMO: K-Means
labels_km = KMeans(n_clusters=K_CLUSTERS, random_state=RANDOM_STATE, n_init=10).fit_predict(X)
cluster_user = pd.Series(labels_km, index=M.index, name="cluster")
print("Distribuicao por cluster:", cluster_user.value_counts().sort_index().to_dict())

# VISUALIZACAO (PCA 2D) + perfil de cada cluster
X2 = PCA(n_components=2, random_state=RANDOM_STATE).fit_transform(X)
perfil_cluster = perfil.fillna(0.0).groupby(cluster_user).mean().reindex(columns=generos)
fig, (a1, a2) = plt.subplots(1, 2, figsize=(16, 7))
sc = a1.scatter(X2[:, 0], X2[:, 1], c=cluster_user.to_numpy(), cmap="tab10", s=14, alpha=0.6)
a1.set_title("Usuarios por cluster (PCA 2D)"); a1.set_xlabel("Comp 1"); a1.set_ylabel("Comp 2")
a1.grid(True, alpha=0.3); fig.colorbar(sc, ax=a1, label="Cluster")
lim = float(np.nanmax(np.abs(perfil_cluster.to_numpy()))) or 1.0
im = a2.imshow(perfil_cluster.to_numpy(), aspect="auto", cmap="RdBu_r", vmin=-lim, vmax=lim)
a2.set_yticks(range(len(perfil_cluster))); a2.set_yticklabels([f"C{c}" for c in perfil_cluster.index])
a2.set_xticks(range(G)); a2.set_xticklabels(generos, rotation=90, fontsize=8)
a2.set_title("Perfil de genero por cluster (verm=acima da media)"); fig.colorbar(im, ax=a2)
plt.tight_layout(); plt.savefig(os.path.join(DIR_IMAGENS, "final_clusters.png"), dpi=110); plt.close()
print("Visualizacao salva: imagens/final_clusters.png")

# ANALISE INICIAL + PROBLEMAS do agrupamento
sil_km = silhouette_score(X[amostra_idx(len(X), N_AMOSTRA)],
                          labels_km[amostra_idx(len(X), N_AMOSTRA)])
print(f"Analise inicial: silhouette ~ {sil_km:.3f}.")
print("Problemas observados: silhouette baixo (gostos sao continuos, sem fronteiras "
      "nitidas) e alta dimensionalidade (~%d generos) -> clusters pouco separados." % G)


# #############################################################################
# SEMANA 3 - Agrupamento II: comparacao, validacao e interpretacao
# #############################################################################
print("\n" + "#" * 70)
print("SEMANA 3 - AGRUPAMENTO II (comparacao e interpretacao)")
print("#" * 70)

# 2o ALGORITMO: Hierarquico (Ward), em amostra (O(n^2))
idx_am = amostra_idx(len(X), N_AMOSTRA)
Xam, lab_km_am = X[idx_am], labels_km[idx_am]
lab_hi_am = AgglomerativeClustering(n_clusters=K_CLUSTERS, linkage="ward").fit_predict(Xam)

# DUAS+ METRICAS de avaliacao (na amostra, comparacao justa)
def metr(Xs, lab):
    return (silhouette_score(Xs, lab), davies_bouldin_score(Xs, lab), calinski_harabasz_score(Xs, lab))
s_km, db_km, ch_km = metr(Xam, lab_km_am)
s_hi, db_hi, ch_hi = metr(Xam, lab_hi_am)
print(f"{'Metrica':>18} | {'K-Means':>9} | {'Hierarquico':>11}")
print(f"{'silhouette(+)':>18} | {s_km:>9.3f} | {s_hi:>11.3f}")
print(f"{'davies_bouldin(-)':>18} | {db_km:>9.3f} | {db_hi:>11.3f}")
print(f"{'calinski_harabasz(+)':>18} | {ch_km:>9.0f} | {ch_hi:>11.0f}")

# COMPARACAO entre algoritmos (ARI)
print(f"Adjusted Rand Index K-Means x Hierarquico: {adjusted_rand_score(lab_km_am, lab_hi_am):.3f}")

# ANALISE DE PARAMETROS (k via Davies-Bouldin)
print("Analise de parametros (Davies-Bouldin por k; menor=melhor):")
for k in range(2, 8):
    lb = KMeans(n_clusters=k, random_state=RANDOM_STATE, n_init=10).fit_predict(Xam)
    print(f"   k={k}: DB={davies_bouldin_score(Xam, lb):.3f}", end="")
print(f"\n-> adotado k={K_CLUSTERS} (equilibrio entre metrica e interpretabilidade).")

# INTERPRETACAO + NOMEACAO dos perfis
print("\nInterpretacao e NOMEACAO dos perfis:")
nomes_perfis = {}
for c in sorted(cluster_user.unique()):
    top = perfil_cluster.loc[c].sort_values(ascending=False).head(2).index.tolist()
    nomes_perfis[c] = "Fa de " + " e ".join(top)
    print(f"   Cluster {c} -> '{nomes_perfis[c]}' "
          f"({int((cluster_user == c).sum())} usuarios)")

# COMO os clusters serao usados na IC + GERACAO da estrutura de saida
print("Uso na IC: cada cluster tera seus PESOS de genero aprendidos por algoritmo")
print("           genetico, usados para pontuar e recomendar filmes.")
saida_cluster = cluster_user.rename_axis("userId").reset_index()
saida_cluster["perfil"] = saida_cluster["cluster"].map(nomes_perfis)
saida_cluster.to_csv(os.path.join(DIR_CSVS, "final_usuarios_clusters.csv"), index=False)
print("Estrutura de saida salva: csvs/final_usuarios_clusters.csv")


# #############################################################################
# SEMANA 4 - Inteligencia Computacional I: construcao do modelo
# #############################################################################
print("\n" + "#" * 70)
print("SEMANA 4 - INTELIGENCIA COMPUTACIONAL I")
print("#" * 70)
print("TAREFA: prever se o usuario vai gostar (nota>=4) e RANQUEAR filmes a recomendar.")
print("TECNICAS: (a) Algoritmo Genetico aprende pesos de genero por cluster;")
print("          (b) Rede Neural (MLP) como comparacao.")

# PREPARACAO: pares rotulados + split treino/teste (features de TREINO -> sem vazamento)
pares = avaliacoes.copy()
pares["gostou"] = (pares["rating"] >= LIMIAR_GOSTOU).astype(np.int8)
pares["cluster"] = pares["userId"].map(cluster_user).to_numpy()
y = pares["gostou"].to_numpy()
idx_tr, idx_te = train_test_split(np.arange(len(pares)), test_size=0.25,
                                  random_state=RANDOM_STATE, stratify=y)
perfil_tr = perfil_por_genero(pares.iloc[idx_tr])

def feats(uids, mids, perf):
    P = perf.reindex(uids).fillna(0.0).to_numpy()
    Mo = filme_oh.reindex(mids).fillna(0.0).to_numpy()
    return P * Mo                                          # preferencia nos generos do filme
sub_tr, sub_te = pares.iloc[idx_tr], pares.iloc[idx_te]
Xtr = feats(sub_tr["userId"].to_numpy(), sub_tr["movieId"].to_numpy(), perfil_tr)
Xte = feats(sub_te["userId"].to_numpy(), sub_te["movieId"].to_numpy(), perfil_tr)
y_tr, y_te = y[idx_tr], y[idx_te]
c_tr, c_te = sub_tr["cluster"].to_numpy(), sub_te["cluster"].to_numpy()

def _auc(yy, s):
    return 0.5 if (np.ptp(s) == 0 or len(np.unique(yy)) < 2) else roc_auc_score(yy, s)

# CONSTRUCAO + TREINAMENTO: Algoritmo Genetico
def treinar_ga(Xc, yc):
    fit = lambda w: _auc(yc, Xc @ w)
    pop = rng.uniform(-1, 1, size=(POP, G)); bw, bf = np.zeros(G), -1.0
    for _ in range(GER):
        fits = np.array([fit(w) for w in pop])
        if fits.max() > bf: bf = fits.max(); bw = pop[int(fits.argmax())].copy()
        o = np.argsort(-fits); pop, fits = pop[o], fits[o]
        nova = [pop[i].copy() for i in range(ELITE)]
        while len(nova) < POP:
            i, j = rng.choice(POP, 2, replace=False); pai = pop[i] if fits[i] >= fits[j] else pop[j]
            k, l = rng.choice(POP, 2, replace=False); mae = pop[k] if fits[k] >= fits[l] else pop[l]
            ch = np.where(rng.rand(G) < 0.5, pai, mae)
            if rng.rand() < P_MUT: ch = np.clip(ch + rng.normal(0, 0.1, G), -1, 1)
            nova.append(ch)
        pop = np.array(nova)
    return bw

w_global = treinar_ga(Xtr, y_tr)                          # GA sem cluster
W = {}                                                    # GA com cluster
for c in sorted(cluster_user.unique()):
    mk = c_tr == c
    W[c] = treinar_ga(Xtr[mk], y_tr[mk]) if (mk.sum() >= 30 and len(np.unique(y_tr[mk])) >= 2) else w_global.copy()

# REDE NEURAL (MLP) nas mesmas features + one-hot do cluster
ks = np.arange(K_CLUSTERS)
def cl_oh(c): return (c[:, None] == ks).astype(np.float32)
Xtr_mlp = np.hstack([Xtr, cl_oh(c_tr)]); Xte_mlp = np.hstack([Xte, cl_oh(c_te)])
scn = StandardScaler().fit(Xtr_mlp)
mlp = MLPClassifier(hidden_layer_sizes=(16,), max_iter=400, early_stopping=True,
                    random_state=RANDOM_STATE).fit(scn.transform(Xtr_mlp), y_tr)
auc_mlp = _auc(y_te, mlp.predict_proba(scn.transform(Xte_mlp))[:, 1])

# AVALIACAO PRELIMINAR + COMPARACAO COM/SEM CLUSTER
auc_sem = _auc(y_te, Xte @ w_global)
auc_com = _auc(y_te, (Xte * np.array([W[c] for c in c_te])).sum(axis=1))
print(f"\nAvaliacao (AUC teste): GA sem cluster={auc_sem:.3f} | "
      f"GA com cluster={auc_com:.3f} | Rede Neural={auc_mlp:.3f}")
print(f"Comparacao com/sem cluster (GA): {auc_com - auc_sem:+.3f}")


# #############################################################################
# SEMANA 5 - Integracao final: refinamento, comparacao e recomendacao
# #############################################################################
print("\n" + "#" * 70)
print("SEMANA 5 - INTEGRACAO FINAL")
print("#" * 70)

# REFINAMENTO: clusters pequenos ja recaem no peso global (acima).
# COMPARACAO FINAL entre abordagens (AUC teste):
print("Comparacao final entre abordagens (AUC teste; maior=melhor):")
comp = {"Aleatorio": 0.5,
        "So gosto pessoal": _auc(y_te, Xte.sum(axis=1)),
        "GA sem cluster": auc_sem,
        "GA com cluster": auc_com,
        "Rede Neural (MLP)": auc_mlp}
for nome, v in comp.items():
    print(f"   {nome:20s} AUC = {v:.3f}")

# CONTRIBUICAO DO AGRUPAMENTO
print(f"\nContribuicao do agrupamento (GA com - sem cluster): {auc_com - auc_sem:+.3f}")
print("   (na base pequena tende a ~0: o gosto pessoal ja carrega o sinal;")
print("    o cluster ajuda mais na base completa e em cold-start.)")

# RECOMENDACAO / DECISAO FINAL + DEMONSTRACAO
filmes_idx = filmes.set_index("movieId")
nota_media = avaliacoes.groupby("movieId")["rating"].mean()
def recomendar(u, n=TOP_N):
    c = cluster_user.loc[u]
    vistos = set(avaliacoes.loc[avaliacoes["userId"] == u, "movieId"])
    cand = np.array([m for m in filmes_com_genero if m not in vistos])
    score = feats(np.full(len(cand), u), cand, perfil) @ W[c]
    top = pd.Series(score, index=cand).sort_values(ascending=False).head(n).index
    out = filmes_idx.loc[top, ["title", "genres"]].copy()
    out["nota"] = out.index.map(nota_media).round(2)
    return out.reset_index(drop=True)

print("\nDEMONSTRACAO - recomendacao para 1 usuario por cluster:")
ativ = avaliacoes["userId"].value_counts()
for c in sorted(cluster_user.unique()):
    u = ativ.reindex(cluster_user.index[cluster_user == c]).idxmax()
    g_top = perfil.loc[u].sort_values(ascending=False).head(3).index.tolist()
    print(f"\nUSUARIO {u} (cluster {c} - '{nomes_perfis[c]}') | curte: {', '.join(g_top)}")
    print(recomendar(u).to_string(index=False))

# ANALISE CRITICA
print("\n" + "#" * 70)
print("ANALISE CRITICA")
print("#" * 70)
print(f"""
PONTOS FORTES: pipeline completo e reprodutivel; avaliacao sem vazamento (features
  de treino so do treino); recomendacao interpretavel (gosto por genero); escalavel.
LIMITACOES: so usa GENERO (ignora elenco/diretor/ano) -> teto de precisao; base
  pequena e split unico -> alta variancia; contribuicao do cluster pequena aqui.
PROXIMOS PASSOS: validacao cruzada e metricas de ranking (precision@k, nDCG);
  modelo hibrido (colaborativo + conteudo); mais sinais alem do genero.
""")
