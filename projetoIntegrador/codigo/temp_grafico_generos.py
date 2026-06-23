# =============================================================================
# *** SCRIPT TEMPORARIO / EXPLORATORIO ***  (pode apagar depois)
# -----------------------------------------------------------------------------
# Grafico: usuarios por genero. Para cada (usuario, genero) com >=1 avaliacao:
#   eixo Y      = nota MEDIA do usuario naquele genero (0.5 a 5.0)
#   eixo X      = faixa do genero; DENTRO da faixa, a posicao horizontal cresce
#                 com o VOLUME (nº de filmes que o usuario viu no genero) ->
#                 quem viu mais filmes do genero fica MAIS A DIREITA na faixa.
#   cor do ponto= cluster do USUARIO (KMeans no perfil de notas por genero)
#   fundo cinza = cluster do GENERO (colunas agrupadas por gosto parecido)
#
# Duas clusterizacoes (coisas diferentes):
#   - USUARIOS: KMeans -> cor dos pontos (que tipo de avaliador).
#   - GENEROS : clustering HIERARQUICO (Ward) sobre genero x usuario -> agrupa
#               as COLUNAS por similaridade (generos avaliados de forma parecida
#               ficam adjacentes, com mesmo fundo cinza). Hierarquico e ideal p/
#               agrupar poucos itens (≈19 generos).
#
# NAO faz parte do pipeline final; exploratorio, base PEQUENA.
# =============================================================================
import os
import numpy as np
import pandas as pd
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans, AgglomerativeClustering

# --- Caminhos relativos a RAIZ do projeto (este script fica em codigo/) ------
RAIZ = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DIR_IMAGENS = os.path.join(RAIZ, "imagens")
os.makedirs(DIR_IMAGENS, exist_ok=True)

# --- Selecao do dataset (escolha a cada execucao) ---------------------------
print("Qual dataset usar?  1 = ml-latest-small (rapido)  |  2 = ml-latest (completa ~33M)")
_op = input("Digite 1 ou 2: ").strip()
BASE = "ml-latest" if _op == "2" else "ml-latest-small"
PASTA = os.path.join(RAIZ, BASE) + os.sep        # datasets ficam na RAIZ do projeto
print(f"Dataset escolhido: {PASTA}\n")

K_USER       = 4       # clusters de usuarios (cor dos pontos)
K_GEN        = 4       # clusters de generos (agrupamento das colunas)
RANDOM_STATE = 42
HALF         = 0.40    # meia-largura da faixa de cada genero

# -----------------------------------------------------------------------------
# 1. Dados
# -----------------------------------------------------------------------------
filmes = pd.read_csv(PASTA + "movies.csv", usecols=["movieId", "genres"])
avaliacoes = pd.read_csv(PASTA + "ratings.csv", usecols=["userId", "movieId", "rating"])

# Na base COMPLETA (2), amostra usuarios para o grafico nao tentar plotar
# dezenas de milhoes de pontos (a base pequena (1) usa todos os usuarios).
N_AMOSTRA_USERS = 10000
if BASE == "ml-latest":
    uids = avaliacoes["userId"].unique()
    if len(uids) > N_AMOSTRA_USERS:
        sel = pd.Series(uids).sample(N_AMOSTRA_USERS, random_state=RANDOM_STATE)
        avaliacoes = avaliacoes[avaliacoes["userId"].isin(sel)]
        print(f"Base completa: amostrados {N_AMOSTRA_USERS} de {len(uids)} usuarios.")

filmes["genres_list"] = filmes["genres"].fillna("(no genres listed)").str.split("|")

fg = filmes[["movieId", "genres_list"]].explode("genres_list").reset_index(drop=True)
fg = fg[fg["genres_list"] != "(no genres listed)"]
av_gen = avaliacoes.merge(fg, on="movieId")

# -----------------------------------------------------------------------------
# 2. Por (usuario, genero): nota MEDIA e VOLUME (nº de filmes)
# -----------------------------------------------------------------------------
stats_ug = (av_gen.groupby(["userId", "genres_list"])["rating"]
            .agg(media_genero="mean", n_filmes="count").reset_index())

# -----------------------------------------------------------------------------
# 3. CLUSTER DOS USUARIOS (cor dos pontos) - KMeans sobre usuario x genero
# -----------------------------------------------------------------------------
matriz = stats_ug.pivot(index="userId", columns="genres_list", values="media_genero")
matriz_p = matriz.T.fillna(matriz.mean(axis=1)).T          # NaN -> media do proprio usuario
matriz_p = matriz_p.fillna(matriz_p.mean())                # seguranca
Xu = StandardScaler().fit_transform(matriz_p.to_numpy())
k_user = min(K_USER, matriz.shape[0])
cl_user = KMeans(n_clusters=k_user, random_state=RANDOM_STATE, n_init=10).fit_predict(Xu)
cluster_user = pd.Series(cl_user, index=matriz.index, name="cluster")
stats_ug = stats_ug.merge(cluster_user, on="userId")

# -----------------------------------------------------------------------------
# 4. CLUSTER DOS GENEROS (agrupar as COLUNAS) - HIERARQUICO (Ward)
#    Genero descrito pelo vetor de notas (centralizadas por usuario) entre todos
#    os usuarios; generos avaliados de forma parecida caem no mesmo grupo.
# -----------------------------------------------------------------------------
genero_user = stats_ug.pivot(index="genres_list", columns="userId", values="media_genero")
genero_user_c = genero_user.sub(genero_user.mean(axis=0), axis=1).fillna(0.0)  # centra por usuario
Xg = StandardScaler().fit_transform(genero_user_c.to_numpy())
k_gen = min(K_GEN, genero_user.shape[0])
cl_gen = AgglomerativeClustering(n_clusters=k_gen, linkage="ward").fit_predict(Xg)
cluster_genero = pd.Series(cl_gen, index=genero_user.index, name="cluster_g")

# -----------------------------------------------------------------------------
# 5. ORDENA as colunas: generos do MESMO cluster ficam adjacentes;
#    clusters e generos ordenados por VOLUME (mais assistido a direita)
# -----------------------------------------------------------------------------
vol_por_genero = av_gen.groupby("genres_list").size()
gen_info = pd.DataFrame({"genero": cluster_genero.index, "cluster_g": cluster_genero.to_numpy()})
gen_info["vol"] = vol_por_genero.reindex(gen_info["genero"]).to_numpy()
ordem_cluster = gen_info.groupby("cluster_g")["vol"].sum().sort_values(ascending=False).index
rank_cluster = {c: i for i, c in enumerate(ordem_cluster)}
gen_info["rank_c"] = gen_info["cluster_g"].map(rank_cluster)
gen_info = gen_info.sort_values(["rank_c", "vol"], ascending=[True, False]).reset_index(drop=True)
generos_ordem = gen_info["genero"].tolist()
xpos = {g: i for i, g in enumerate(generos_ordem)}

# -----------------------------------------------------------------------------
# 6. POSICAO X dentro da faixa = VOLUME do usuario no genero (mais -> direita)
#    Normaliza log(nº de filmes) entre 0 e 1 DENTRO de cada genero.
# -----------------------------------------------------------------------------
logn = np.log1p(stats_ug["n_filmes"])
gmin = logn.groupby(stats_ug["genres_list"]).transform("min")
gmax = logn.groupby(stats_ug["genres_list"]).transform("max")
rng_ = gmax - gmin
ratio = (logn - gmin) / rng_.where(rng_ > 0, 1)
norm = ratio.where(rng_ > 0, 0.5)                          # genero com volume unico -> centro
stats_ug["x"] = stats_ug["genres_list"].map(xpos) + (norm - 0.5) * 2 * HALF

# -----------------------------------------------------------------------------
# 7. GRAFICO
# -----------------------------------------------------------------------------
fig, ax = plt.subplots(figsize=(17, 8))

# Fundo: faixas sombreadas por cluster de GENERO (colunas agrupadas)
for c in ordem_cluster:
    posicoes = gen_info.index[gen_info["cluster_g"] == c]
    x0, x1 = posicoes.min() - 0.5, posicoes.max() + 0.5
    cor = "#eeeeee" if rank_cluster[c] % 2 == 0 else "#dcdcdc"
    ax.axvspan(x0, x1, color=cor, zorder=0)
    ax.text((x0 + x1) / 2, 5.18, f"Grupo de generos {rank_cluster[c] + 1}",
            ha="center", va="bottom", fontsize=9, fontweight="bold")

sc = ax.scatter(stats_ug["x"], stats_ug["media_genero"],
                c=stats_ug["cluster"], cmap="tab10", s=12, alpha=0.55, zorder=2)

rotulos_x = [f"{g} ({int(vol_por_genero[g])})" for g in generos_ordem]
ax.set_xticks(range(len(generos_ordem)))
ax.set_xticklabels(rotulos_x, rotation=45, ha="right")
ax.set_xlabel("Genero (colunas agrupadas por cluster de genero) | dentro da faixa: "
              "mais a DIREITA = usuario viu MAIS filmes daquele genero")
ax.set_ylabel("Nota media do usuario naquele genero (0.5 a 5.0)")
ax.set_ylim(0.4, 5.35)
ax.set_title("[TEMPORARIO] Usuarios por genero\n"
             "cor do ponto = cluster do USUARIO  |  fundo cinza = grupo de generos parecidos "
             "(clustering hierarquico)")
fig.colorbar(sc, ax=ax, label="Cluster do USUARIO")
ax.grid(True, axis="y", alpha=0.3)
plt.tight_layout()
plt.savefig(os.path.join(DIR_IMAGENS, "temp_usuarios_por_genero.png"), dpi=110)
plt.close()

print(f"Usuarios: {matriz.shape[0]} | generos: {genero_user.shape[0]} | "
      f"pontos: {len(stats_ug)} | clusters de genero: {k_gen}")
print("\nGrupos de generos encontrados:")
for c in ordem_cluster:
    membros = gen_info.loc[gen_info["cluster_g"] == c, "genero"].tolist()
    print(f"  Grupo {rank_cluster[c] + 1}: " + ", ".join(membros))
print("\nGrafico salvo: temp_usuarios_por_genero.png")
