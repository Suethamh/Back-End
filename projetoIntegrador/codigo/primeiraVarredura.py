import os
import numpy as np
import pandas as pd
from scipy import sparse
from scipy.cluster.hierarchy import linkage, dendrogram
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans, MiniBatchKMeans, AgglomerativeClustering
from sklearn.decomposition import PCA
from sklearn.metrics import (silhouette_score, davies_bouldin_score,
                             calinski_harabasz_score, adjusted_rand_score)
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

pd.set_option("display.max_columns", None)
pd.set_option("display.width", 200)

# --- Caminhos relativos a RAIZ do projeto (este script fica em codigo/) ------
RAIZ = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DIR_IMAGENS = os.path.join(RAIZ, "imagens")
DIR_CSVS = os.path.join(RAIZ, "csvs")
os.makedirs(DIR_IMAGENS, exist_ok=True)
os.makedirs(DIR_CSVS, exist_ok=True)

# --- Selecao do dataset (escolha a cada execucao) ---------------------------
print("Qual dataset usar?  1 = ml-latest-small (rapido)  |  "
      "2 = ml-latest (completa ~33M de avaliacoes, demora alguns minutos)")
_op = input("Digite 1 ou 2: ").strip()
BASE = "ml-latest" if _op == "2" else "ml-latest-small"
PASTA = os.path.join(RAIZ, BASE) + os.sep        # datasets ficam na RAIZ do projeto
print(f"Dataset escolhido: {PASTA}\n")

# ---- Parametros que fazem o pipeline se auto-ajustar a base pequena OU grande
RANDOM_STATE        = 42
K_IDEAL             = 4
LIMITE_KMEANS_EXATO = 50_000
N_AMOSTRA_HIER      = 8_000
SAMPLE_SIL          = 10_000   
QUANTIL_M           = 0.90


print("\n" + "=" * 70)
print("SEMANA 1 - CARREGAMENTO")
print("=" * 70)

filmes = pd.read_csv(
    PASTA + "movies.csv",
    usecols=["movieId", "title", "genres"],
    dtype={"movieId": "int32", "title": "string", "genres": "string"},
)
avaliacoes = pd.read_csv(
    PASTA + "ratings.csv",
    usecols=["userId", "movieId", "rating", "timestamp"],
    dtype={"userId": "int32", "movieId": "int32",
           "rating": "float32", "timestamp": "int64"},
)
tags = pd.read_csv(
    PASTA + "tags.csv",
    usecols=["userId", "movieId", "tag", "timestamp"],
    dtype={"userId": "int32", "movieId": "int32",
           "tag": "string", "timestamp": "int64"},
)
links = pd.read_csv(
    PASTA + "links.csv",
    usecols=["movieId", "imdbId", "tmdbId"],
    dtype={"movieId": "int32", "imdbId": "Int32", "tmdbId": "Int32"},
)

dataset = {"filmes": filmes, "avaliacoes": avaliacoes, "tags": tags, "links": links}

print("\nVisao do dataset:")
for nome, df in dataset.items():
    print(f"\n--- {nome.upper()} ---  dimensao: {df.shape}")
    print(df.dtypes)


# =============================================================================
# VALORES AUSENTES
# =============================================================================
print("\n" + "=" * 70)
print("VALORES AUSENTES")
print("=" * 70)
for nome, df in dataset.items():
    print(f"{nome:10s} - total de ausentes: {int(df.isnull().sum().sum())}")

print("\nRemovendo valores ausentes...")
for nome in dataset:
    antes = len(dataset[nome])
    if nome == "links":
        # links: nao descartar um filme so por falta de tmdbId (a IC pode usar)
        dataset[nome].dropna(subset=["movieId"], inplace=True)
    else:
        dataset[nome].dropna(inplace=True)
    print(f"{nome:10s} - {antes} -> {len(dataset[nome])} "
          f"(removidos: {antes - len(dataset[nome])})")
# Re-vincula os nomes (dropna inplace mantem o mesmo objeto, mas por clareza):
filmes, avaliacoes, tags, links = (dataset["filmes"], dataset["avaliacoes"],
                                   dataset["tags"], dataset["links"])


# =============================================================================
# DUPLICIDADES
# =============================================================================
print("\n" + "=" * 70)
print("DUPLICIDADES")
print("=" * 70)
for nome, df in dataset.items():
    print(f"{nome:10s} - linhas totalmente duplicadas: {int(df.duplicated().sum())}")

filmes.drop_duplicates(subset=["movieId"], inplace=True)
avaliacoes.drop_duplicates(subset=["userId", "movieId"], keep="last", inplace=True)
tags.drop_duplicates(subset=["userId", "movieId", "tag"], inplace=True)
links.drop_duplicates(subset=["movieId"], inplace=True)


# =============================================================================
# OUTLIERS
# =============================================================================
print("\n" + "=" * 70)
print("OUTLIERS")
print("=" * 70)
print(avaliacoes["rating"].describe())

# 1) Notas fora do intervalo valido [0.5, 5.0]
antes = len(avaliacoes)
avaliacoes = avaliacoes[avaliacoes["rating"].between(0.5, 5.0)]
print(f"\nNotas fora de [0.5, 5.0] removidas: {antes - len(avaliacoes)}")

# 2) Filmes com 5 ou menos avaliacoes (cold start)
contagem = avaliacoes["movieId"].value_counts()
print(contagem.describe())
filmes_validos = contagem[contagem > 5].index

ids_validos = np.intersect1d(filmes["movieId"].unique(),
                             np.asarray(filmes_validos))
antes_fl, antes_av = len(filmes), len(avaliacoes)
filmes = filmes[filmes["movieId"].isin(ids_validos)]
avaliacoes = avaliacoes[avaliacoes["movieId"].isin(ids_validos)]
print(f"Filmes removidos: {antes_fl - len(filmes)} | "
      f"Avaliacoes removidas: {antes_av - len(avaliacoes)}")
print(f"Avaliacoes restantes: {len(avaliacoes)}")


# =============================================================================
# TRANSFORMACAO DE ATRIBUTOS
# =============================================================================
filmes["genres_list"] = filmes["genres"].fillna("(no genres listed)").astype(str).str.split("|")
avaliacoes["datetime"] = pd.to_datetime(avaliacoes["timestamp"], unit="s")
tags["datetime"] = pd.to_datetime(tags["timestamp"], unit="s")


# =============================================================================
# NOTA PONDERADA BAYESIANA POR FILME  (decide se o filme e "bom")
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("NOTA PONDERADA BAYESIANA POR FILME")
print("=" * 70)

stats_filme = (avaliacoes.groupby("movieId", sort=False)["rating"]
               .agg(v="count", R="mean"))
C_GLOBAL = float(avaliacoes["rating"].mean())
m_prior = float(stats_filme["v"].quantile(QUANTIL_M))
v, R = stats_filme["v"], stats_filme["R"]
stats_filme["nota_bayesiana"] = (v / (v + m_prior)) * R + (m_prior / (v + m_prior)) * C_GLOBAL

LIMIAR_BOM = float(stats_filme["nota_bayesiana"].quantile(0.75))
stats_filme["filme_bom"] = stats_filme["nota_bayesiana"] >= LIMIAR_BOM

print(f"C (media global) = {C_GLOBAL:.4f} | m (quantil {QUANTIL_M}) = {m_prior:.1f}")
print(f"Limiar 'filme bom' (nota bayesiana, q0.75) = {LIMIAR_BOM:.4f}")
print(f"Filmes classificados como 'bons': "
      f"{int(stats_filme['filme_bom'].sum())} de {len(stats_filme)}")

# Anexa os sinais de qualidade ao catalogo de filmes
filmes = filmes.merge(
    stats_filme.rename(columns={"v": "qtd_avaliacoes", "R": "media_nota"}),
    on="movieId", how="left",
)
filmes["nota_bayesiana"] = filmes["nota_bayesiana"].fillna(C_GLOBAL).astype("float32")
filmes["filme_bom"] = filmes["filme_bom"].fillna(False).astype(bool)


# =============================================================================
# PRIMEIRA ANALISE EXPLORATORIA
# =============================================================================
print("\n" + "=" * 70)
print("PRIMEIRA ANALISE EXPLORATORIA")
print("=" * 70)
print(f"Usuarios unicos    : {avaliacoes['userId'].nunique()}")
print(f"Filmes unicos      : {avaliacoes['movieId'].nunique()}")
print(f"Total de avaliacoes: {len(avaliacoes)}")

print("\nTOP 10 generos mais frequentes:")
print(filmes.explode("genres_list")["genres_list"].value_counts().head(10))

print("\nTOP 10 filmes mais avaliados (popularidade):")
top_pop = (avaliacoes.groupby("movieId").size()
           .sort_values(ascending=False).head(10)
           .reset_index(name="qtd_avaliacoes")
           .merge(filmes[["movieId", "title"]], on="movieId"))
print(top_pop[["title", "qtd_avaliacoes"]].to_string(index=False))

# Antes era "melhor media simples (>=50)" - agora usamos a nota bayesiana,
# que ja desconta filmes com poucos votos de forma estatistica.
print("\nTOP 10 filmes por NOTA BAYESIANA (qualidade):")
top_bayes = (filmes.sort_values("nota_bayesiana", ascending=False).head(10))
print(top_bayes[["title", "media_nota", "qtd_avaliacoes",
                 "nota_bayesiana", "filme_bom"]].to_string(index=False))


# =============================================================================
# SEMANA 2 - SELECAO DE ATRIBUTOS + AGRUPAMENTO INICIAL
# =============================================================================
print("\n" + "=" * 70)
print("SEMANA 2 - ATRIBUTOS PARA AGRUPAMENTO")
print("=" * 70)

# ---- Atributos comportamentais por usuario ---------------------------------
perfil = avaliacoes.groupby("userId").agg(
    media_nota=("rating", "mean"),
    desvio_nota=("rating", "std"),
    qtd_avaliacoes=("rating", "size"),
)
perfil["desvio_nota"] = perfil["desvio_nota"].fillna(0)

generos = sorted({g for lst in filmes["genres_list"] for g in lst
                  if g and g != "(no genres listed)"})
genero_idx = {g: j for j, g in enumerate(generos)}

user_ids = np.sort(avaliacoes["userId"].unique())
movie_ids = np.sort(filmes["movieId"].unique())
user_pos = pd.Series(np.arange(len(user_ids)), index=user_ids)
movie_pos = pd.Series(np.arange(len(movie_ids)), index=movie_ids)

# Matriz esparsa FILME x GENERO (one-hot) - explode so dos FILMES (poucas linhas)
fg = filmes[["movieId", "genres_list"]].explode("genres_list").dropna(subset=["genres_list"])
fg = fg[fg["genres_list"].isin(set(genero_idx))]   # remove "(no genres listed)" / vazios
fg_rows = fg["movieId"].map(movie_pos).to_numpy().astype(np.int64)
fg_cols = fg["genres_list"].map(genero_idx).to_numpy().astype(np.int64)
M_fg = sparse.csr_matrix(
    (np.ones(len(fg), dtype=np.float32), (fg_rows, fg_cols)),
    shape=(len(movie_ids), len(generos)),
)

# Matriz esparsa USUARIO x FILME (incidencia 0/1, ja que ha 1 nota por par)
uf_rows = avaliacoes["userId"].map(user_pos).to_numpy().astype(np.int64)
uf_cols = avaliacoes["movieId"].map(movie_pos).to_numpy().astype(np.int64)
M_uf = sparse.csr_matrix(
    (np.ones(len(avaliacoes), dtype=np.float32), (uf_rows, uf_cols)),
    shape=(len(user_ids), len(movie_ids)),
)

# Produto esparso -> contagens usuario x genero -> normaliza por linha
C_ug = np.asarray((M_uf @ M_fg).todense(), dtype=np.float64)
soma_linha = C_ug.sum(axis=1, keepdims=True)
soma_linha[soma_linha == 0] = 1.0
generos_pct = pd.DataFrame(C_ug / soma_linha,
                           index=pd.Index(user_ids, name="userId"),
                           columns=generos)

# Vetor final por usuario (comportamento + preferencia de genero)
perfil_usuario = perfil.join(generos_pct, how="left").fillna(0)
feature_cols = perfil_usuario.columns.tolist()   # 3 comportamentais + generos
print(f"Usuarios: {perfil_usuario.shape[0]} | atributos de clustering: {len(feature_cols)}")


print("\n" + "=" * 70)
print("PESOS POR USUARIO (para a IC)")
print("=" * 70)

def percentil_rank(s):
    return s.rank(method="average", pct=True)

idx_usuarios = perfil.index

# peso_tags (usuarios sem tags -> 0; zeros NAO entram no ranking)
tags_por_usuario = (tags.groupby("userId").size()
                    .reindex(idx_usuarios, fill_value=0).astype(float))
sig_tags = np.log1p(tags_por_usuario)
# Quem nao cria tags -> 0. Quem cria -> mapeado em (0.5, 1.0] para ficar
# claramente acima de todos os nao-taggers (rankear so o subconjunto e
# desloca-lo para a metade superior evita colisao com o "mar de zeros").
peso_tags = pd.Series(0.0, index=idx_usuarios)
mask_tag = sig_tags > 0
if mask_tag.any():
    peso_tags.loc[mask_tag] = 0.5 + 0.5 * percentil_rank(sig_tags[mask_tag])

# peso_notas (volume de avaliacoes)
peso_notas = percentil_rank(np.log1p(perfil["qtd_avaliacoes"].astype(float)))

# peso_notas_boas (media encolhida por Bayes -> evita inflar quem deu poucas 5.0)
m_user = max(float(perfil["qtd_avaliacoes"].median()), 1.0)
qtd = perfil["qtd_avaliacoes"].astype(float)
media_encolhida = (qtd * perfil["media_nota"].astype(float) + m_user * C_GLOBAL) / (qtd + m_user)
peso_notas_boas = percentil_rank(media_encolhida)

pesos_usuario = pd.DataFrame({
    "peso_tags": peso_tags,
    "peso_notas": peso_notas,
    "peso_notas_boas": peso_notas_boas,
}, index=idx_usuarios).clip(0.0, 1.0)
pesos_usuario.index.name = "userId"
print(pesos_usuario.describe().round(3).loc[["mean", "min", "max"]])


# =============================================================================
# PADRONIZACAO + KMEANS (adaptativo: exato em base pequena, MiniBatch em grande)
# =============================================================================
print("\n" + "=" * 70)
print("SEMANA 2 - AGRUPAMENTO (K-Means)")
print("=" * 70)

X = StandardScaler().fit_transform(perfil_usuario[feature_cols].to_numpy(dtype="float32"))
N_USUARIOS = X.shape[0]

def amostra_idx(n, size, seed=RANDOM_STATE):
    """Indices de uma amostra deterministica (ou todos, se n <= size)."""
    if n <= size:
        return np.arange(n)
    return np.sort(np.random.RandomState(seed).choice(n, size, replace=False))

def criar_kmeans(k, n_init=10):
    if N_USUARIOS <= LIMITE_KMEANS_EXATO:
        return KMeans(n_clusters=k, random_state=RANDOM_STATE, n_init=n_init)
    return MiniBatchKMeans(n_clusters=k, random_state=RANDOM_STATE,
                           n_init=n_init, batch_size=4096, max_iter=100)

usa_minibatch = N_USUARIOS > LIMITE_KMEANS_EXATO
print(f"Algoritmo: {'MiniBatchKMeans (base grande)' if usa_minibatch else 'KMeans exato'}")

# Metodo do cotovelo (Davies-Bouldin). Em base grande roda numa amostra com
# n_init reduzido (so para o grafico), evitando 9x o ajuste completo em 30M.
X_elbow = X[amostra_idx(N_USUARIOS, SAMPLE_SIL, seed=RANDOM_STATE + 1)]
db_indices = {}
for k in range(2, 11):
    lab_k = criar_kmeans(k, n_init=3).fit_predict(X_elbow)
    if len(np.unique(lab_k)) >= 2:                 # evita ValueError em k degenerado
        db_indices[k] = davies_bouldin_score(X_elbow, lab_k)

kmeans = criar_kmeans(K_IDEAL, n_init=10)
labels_kmeans = kmeans.fit_predict(X)
perfil_usuario["cluster_kmeans"] = labels_kmeans
print(f"K escolhido: {K_IDEAL}")
print("Distribuicao:", np.bincount(labels_kmeans, minlength=K_IDEAL).tolist())

# Visualizacao (PCA 2D) - guarda o objeto p/ ler a variancia e a composicao
pca = PCA(n_components=2, random_state=RANDOM_STATE)
X_2d = pca.fit_transform(X)
var1, var2 = pca.explained_variance_ratio_[:2] * 100   # % de informacao por eixo

# Cada eixo e uma COMBINACAO dos atributos. Em vez de "Componente Principal 1/2"
# (que nao diz nada), o rotulo e MONTADO a partir dos atributos que mais pesam em
# cada lado do eixo -> a legenda diz, em palavras, o que significa um usuario
# estar para a direita/esquerda (eixo X) ou em cima/embaixo (eixo Y).
cargas = pd.DataFrame(pca.components_.T, index=feature_cols, columns=["Eixo1", "Eixo2"])

def _legivel(feat):
    nomes = {"media_nota": "da notas altas",
             "desvio_nota": "da notas variadas",
             "qtd_avaliacoes": "avalia muitos filmes"}
    return nomes.get(feat, f"gosta de {feat}")        # generos -> "gosta de Drama"

def descrever_lados(coluna, n=2):
    """Frase do lado positivo e do lado negativo de um eixo (a partir das cargas)."""
    ord_ = cargas[coluna].sort_values(ascending=False)
    pos = " e ".join(_legivel(f) for f in ord_.head(n).index if ord_[f] > 0)
    neg = " e ".join(_legivel(f) for f in ord_.tail(n).index if ord_[f] < 0)
    return pos or "perfil oposto", neg or "perfil oposto"

print("\nComposicao dos eixos da PCA (atributos que mais pesam em cada um):")
for eixo in ["Eixo1", "Eixo2"]:
    top = cargas[eixo].abs().sort_values(ascending=False).head(4).index
    print(f"  {eixo}: " + ", ".join(f"{a} ({cargas.loc[a, eixo]:+.2f})" for a in top))

pos_x, neg_x = descrever_lados("Eixo1")
pos_y, neg_y = descrever_lados("Eixo2")

# Rotulos que dizem o SIGNIFICADO de cada lado (reutilizados nos 2 graficos)
rotulo_x = (f"Esquerda: usuario {neg_x}        Direita: usuario {pos_x}\n"
            f"(eixo que mais separa os perfis - {var1:.0f}% da variacao)")
rotulo_y = (f"Embaixo: usuario {neg_y}     Em cima: usuario {pos_y}\n"
            f"({var2:.0f}% da variacao)")
legenda_pca = ("Cada ponto e um usuario; a POSICAO indica o perfil de preferencia "
               "(veja os extremos de cada eixo). Pontos proximos = perfis parecidos.")

fig, ax = plt.subplots(figsize=(11, 8))
sc = ax.scatter(X_2d[:, 0], X_2d[:, 1], c=labels_kmeans, cmap="tab10", s=30, alpha=0.6)
ax.set_title(f"Clusters de usuarios - K-Means (k={K_IDEAL})\n"
             f"os {len(feature_cols)} atributos comprimidos em 2 eixos pela PCA "
             f"(cada ponto = 1 usuario; proximidade = perfis parecidos)", fontsize=11)
ax.set_xlabel(rotulo_x, fontsize=9)
ax.set_ylabel(rotulo_y, fontsize=9)
ax.grid(True, alpha=0.3)
fig.colorbar(sc, ax=ax, label="Cluster (grupo de usuarios)")
fig.text(0.5, 0.01, legenda_pca, ha="center", fontsize=8, style="italic", wrap=True)
plt.tight_layout(rect=[0, 0.05, 1, 1])
plt.savefig(os.path.join(DIR_IMAGENS, "clusters_usuarios.png"), dpi=110)
plt.close()
print("Grafico salvo: clusters_usuarios.png")


# =============================================================================
# =============================================================================
# SEMANA 3 - COMPARACAO, VALIDACAO E INTERPRETACAO
# =============================================================================
# =============================================================================

# -----------------------------------------------------------------------------
# 1. SEGUNDO ALGORITMO (HIERARQUICO) em AMOSTRA
#    AgglomerativeClustering e scipy.linkage sao O(n^2) de memoria => inviaveis
#    em ~180k usuarios. Rodamos numa amostra representativa (random_state fixo).
#    Na base pequena (<= N_AMOSTRA_HIER) a "amostra" e o dataset inteiro.
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("1. SEGUNDO ALGORITMO: HIERARQUICO (em amostra)")
print("=" * 70)

idx_am = amostra_idx(N_USUARIOS, N_AMOSTRA_HIER, seed=RANDOM_STATE)
X_am = X[idx_am]
lab_km_am = labels_kmeans[idx_am]              # MESMAS linhas que o KMeans
lab_hi_am = AgglomerativeClustering(n_clusters=K_IDEAL, linkage="ward").fit_predict(X_am)
print(f"Amostra usada na comparacao: {len(idx_am)} usuarios")
print("Distribuicao (Hierarquico):", np.bincount(lab_hi_am, minlength=K_IDEAL).tolist())


# -----------------------------------------------------------------------------
# 4. ANALISE DOS PARAMETROS
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("4. ANALISE DOS PARAMETROS")
print("=" * 70)
print("\n[Hierarquico] comparacao de linkage (na amostra):")
for metodo in ["ward", "complete", "average"]:
    rot = AgglomerativeClustering(n_clusters=K_IDEAL, linkage=metodo).fit_predict(X_am)
    print(f"  linkage={metodo:9s} | silhouette={silhouette_score(X_am, rot):.3f} "
          f"| davies_bouldin={davies_bouldin_score(X_am, rot):.3f}")
print("Escolhido 'ward': minimiza a variancia interna dos clusters.")

k_min_db = min(db_indices, key=db_indices.get)
print(f"\n[Metodo do cotovelo] Davies-Bouldin por k (menor = melhor):")
for k, db in db_indices.items():
    print(f"  k={k:2d} | davies_bouldin={db:.3f}" + ("  <- menor DB" if k == k_min_db else ""))
print(f"k de menor DB = {k_min_db}. Adotamos k={K_IDEAL} pelo equilibrio entre "
      f"Davies-Bouldin e interpretabilidade dos perfis.")
print("\nParametros finais:")
print(f"  KMeans      -> n_clusters={K_IDEAL}, n_init=10, random_state={RANDOM_STATE}"
      f" ({'MiniBatch' if usa_minibatch else 'exato'})")
print(f"  Hierarquico -> n_clusters={K_IDEAL}, linkage='ward' (amostra de {len(idx_am)})")


# -----------------------------------------------------------------------------
# 3. METRICAS DE AVALIACAO (3 metricas)
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("3. METRICAS DE AVALIACAO")
print("=" * 70)

def avaliar(Xs, labels):
    return {
        "silhouette": silhouette_score(Xs, labels),          # maior = melhor
        "calinski_harabasz": calinski_harabasz_score(Xs, labels),  # maior = melhor
        "davies_bouldin": davies_bouldin_score(Xs, labels),  # menor = melhor
    }

# Comparacao JUSTA: os dois algoritmos avaliados nas MESMAS linhas (a amostra)
metricas_kmeans = avaliar(X_am, lab_km_am)
metricas_hier = avaliar(X_am, lab_hi_am)

print(f"{'Metrica':>20} | {'K-Means':>10} | {'Hierarquico':>12} | melhor")
print("-" * 62)
for mname in metricas_kmeans:
    a, b = metricas_kmeans[mname], metricas_hier[mname]
    if mname == "davies_bouldin":
        melhor = "K-Means" if a < b else "Hierarquico"
    else:
        melhor = "K-Means" if a > b else "Hierarquico"
    print(f"{mname:>20} | {a:>10.3f} | {b:>12.3f} | {melhor}")

# Metricas do KMeans no dataset INTEIRO (silhouette amostrado em base grande)
sample_size = None if N_USUARIOS <= SAMPLE_SIL else SAMPLE_SIL
sil_full = silhouette_score(X, labels_kmeans, sample_size=sample_size,
                            random_state=RANDOM_STATE)
print(f"\nKMeans no dataset completo ({N_USUARIOS} usuarios): "
      f"silhouette={sil_full:.3f}"
      f"{' (amostral)' if sample_size else ''} | "
      f"DB={davies_bouldin_score(X, labels_kmeans):.3f}")


# -----------------------------------------------------------------------------
# 2. COMPARACAO ENTRE OS ALGORITMOS
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("2. COMPARACAO ENTRE OS ALGORITMOS")
print("=" * 70)

ari = adjusted_rand_score(lab_km_am, lab_hi_am)
print(f"Adjusted Rand Index (na amostra): {ari:.3f}")
if ari > 0.7:
    print("  -> alta concordancia.")
elif ari > 0.4:
    print("  -> concordancia moderada.")
else:
    print("  -> baixa concordancia (os algoritmos enxergam grupos diferentes).")

print("\nTabela cruzada (linhas=KMeans, colunas=Hierarquico):")
print(pd.crosstab(pd.Series(lab_km_am, name="kmeans"),
                  pd.Series(lab_hi_am, name="hier")).to_string())

# Dendrograma (na amostra; linkage tambem e O(n^2))
plt.figure(figsize=(12, 5))
dendrogram(linkage(X_am, method="ward"), truncate_mode="lastp", p=30,
           show_leaf_counts=True)
plt.title(f"Dendrograma - ward (amostra de {len(idx_am)} usuarios)")
plt.xlabel("Usuarios (agrupados)")
plt.ylabel("Distancia")
plt.tight_layout()
plt.savefig(os.path.join(DIR_IMAGENS, "dendrograma.png"), dpi=100)
plt.close()
print("\nGrafico salvo: dendrograma.png")

# Grafico lado a lado K-Means x Hierarquico (na MESMA amostra e no mesmo espaco
# PCA da Semana 2), com os eixos auto-explicativos definidos acima.
X_2d_am = X_2d[idx_am]
fig, axes = plt.subplots(1, 2, figsize=(15, 7))
for ax, labels, titulo in [(axes[0], lab_km_am, "K-Means"),
                           (axes[1], lab_hi_am, "Hierarquico (Ward)")]:
    ax.scatter(X_2d_am[:, 0], X_2d_am[:, 1], c=labels, cmap="tab10", s=30, alpha=0.6)
    ax.set_title(titulo, fontsize=12)
    ax.set_xlabel(rotulo_x, fontsize=9)
    ax.set_ylabel(rotulo_y, fontsize=9)
    ax.grid(True, alpha=0.3)
plt.suptitle(f"Comparacao dos agrupamentos - amostra de {len(idx_am)} usuarios "
             f"(projecao PCA 2D)\ncada ponto = 1 usuario | proximidade = perfis "
             f"parecidos | cores = grupos", fontsize=12)
fig.text(0.5, 0.015, legenda_pca, ha="center", fontsize=8, style="italic", wrap=True)
plt.tight_layout(rect=[0, 0.06, 1, 0.92])
plt.savefig(os.path.join(DIR_IMAGENS, "comparacao_algoritmos.png"), dpi=110)
plt.close()
print("Grafico salvo: comparacao_algoritmos.png")

# Algoritmo de PRODUCAO: KMeans (unico que atribui TODOS os usuarios em escala).
# O hierarquico serve para validacao cruzada (amostra). Por isso cluster_final
# = labels do KMeans.
perfil_usuario["cluster_final"] = labels_kmeans
print("\nAlgoritmo de producao: KMeans (escala para a base real). "
      "Hierarquico usado para validacao em amostra.")


# -----------------------------------------------------------------------------
# 5 e 6. INTERPRETACAO E NOMEACAO DOS PERFIS
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("5 e 6. INTERPRETACAO E NOMEACAO DOS PERFIS")
print("=" * 70)

colunas_genero = generos_pct.columns
media_global = perfil_usuario["media_nota"].mean()
qtd_global = perfil_usuario["qtd_avaliacoes"].mean()

def classificar_engajamento(q):
    if q > 1.3 * qtd_global: return "Engajado"
    if q < 0.7 * qtd_global: return "Casual"
    return "Moderado"

def classificar_tendencia(m):
    if m > media_global + 0.15: return "Generoso"
    if m < media_global - 0.15: return "Critico"
    return "Equilibrado"

nomes_perfis = {}
for c in sorted(perfil_usuario["cluster_final"].unique()):
    grupo = perfil_usuario[perfil_usuario["cluster_final"] == c]
    media_c = grupo["media_nota"].mean()
    qtd_c = grupo["qtd_avaliacoes"].mean()
    genero_top = grupo[colunas_genero].mean().idxmax()
    nome = f"{classificar_engajamento(qtd_c)} {classificar_tendencia(media_c)} - fa de {genero_top}"
    nomes_perfis[c] = nome
    top3 = grupo[colunas_genero].mean().sort_values(ascending=False).head(3)
    print(f"\n--- CLUSTER {c}: \"{nome}\" ---")
    print(f"  Tamanho            : {len(grupo)} ({len(grupo)/len(perfil_usuario)*100:.1f}%)")
    print(f"  Media de nota      : {media_c:.2f} (global {media_global:.2f})")
    print(f"  Avaliacoes/usuario : {qtd_c:.0f} (global {qtd_global:.0f})")
    print(f"  Top 3 generos      : " + ", ".join(f"{g} ({val:.1%})" for g, val in top3.items()))

perfil_usuario["perfil_nome"] = perfil_usuario["cluster_final"].map(nomes_perfis)


# -----------------------------------------------------------------------------
# 7. COMO OS CLUSTERS SERAO USADOS NA INTELIGENCIA COMPUTACIONAL
# -----------------------------------------------------------------------------
# (a) ALVO DE CLASSIFICACAO: a IC aprende a prever o cluster de um usuario NOVO
#     pelas primeiras avaliacoes (cold start).
# (b) FILTRO DE RECOMENDACAO: dentro do cluster, prioriza filmes com filme_bom
#     (nota_bayesiana alta).
# (c) PESOS: a ordem das estrategias de recomendacao (tags / notas / notas_boas)
#     e definida pelo maior peso do usuario (colunas estrategia_1..3 abaixo).


# -----------------------------------------------------------------------------
# 8. ESTRUTURA DE SAIDA DO AGRUPAMENTO (alimenta a IC)
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("8. ESTRUTURA DE SAIDA")
print("=" * 70)

# Junta clusters + pesos por usuario
saida = perfil_usuario[["media_nota", "desvio_nota", "qtd_avaliacoes",
                        "cluster_final", "perfil_nome"]].join(pesos_usuario)

# Ordem de prioridade das estrategias por usuario (maior peso primeiro).
# NAO e a recomendacao (Semana 4) - e so a variavel de saida que a indica.
pesos_cols = ["peso_tags", "peso_notas", "peso_notas_boas"]
rotulo_estr = np.array(["tags", "notas", "notas_boas"])
# kind="stable": em empates (ex.: usuarios sem tags com peso_tags=0) a ordem
# segue pesos_cols (tags > notas > notas_boas) -> saida deterministica.
ordem_idx = np.argsort(-saida[pesos_cols].to_numpy(), axis=1, kind="stable")
saida["estrategia_1"] = rotulo_estr[ordem_idx[:, 0]]
saida["estrategia_2"] = rotulo_estr[ordem_idx[:, 1]]
saida["estrategia_3"] = rotulo_estr[ordem_idx[:, 2]]

saida.reset_index().to_csv(os.path.join(DIR_CSVS, "usuarios_clusterizados.csv"), index=False)
print(f"-> usuarios_clusterizados.csv ({len(saida)} usuarios)")

# Resumo por perfil
resumo = (saida.groupby(["cluster_final", "perfil_nome"])
          .agg(qtd_usuarios=("media_nota", "size"),
               media_nota=("media_nota", "mean"),
               media_avaliacoes=("qtd_avaliacoes", "mean"),
               peso_tags=("peso_tags", "mean"),
               peso_notas=("peso_notas", "mean"),
               peso_notas_boas=("peso_notas_boas", "mean"))
          .round(3).reset_index())
resumo.to_csv(os.path.join(DIR_CSVS, "perfis_clusters.csv"), index=False)
print(f"-> perfis_clusters.csv ({len(resumo)} perfis)")

# Sinais de qualidade por filme (para o filtro de recomendacao da IC)
filmes[["movieId", "title", "media_nota", "qtd_avaliacoes",
        "nota_bayesiana", "filme_bom"]].to_csv(os.path.join(DIR_CSVS, "filmes_nota_bayesiana.csv"), index=False)
print("-> filmes_nota_bayesiana.csv")

print("\nPreview da saida principal:")
print(saida.reset_index().head().to_string(index=False))

# =============================================================================
# SEMANA 4 - INTELIGENCIA COMPUTACIONAL I (modelo inteligente - versao inicial)
# =============================================================================
from sklearn.model_selection import train_test_split
from sklearn.neural_network import MLPClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.dummy import DummyClassifier
from sklearn.metrics import roc_auc_score, accuracy_score, f1_score
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt   # garante 'plt' mesmo se rodar o bloco isolado

MAX_PARES = 200_000  
LIMIAR_GOSTOU = 4.0

print("\n" + "=" * 70)
print("SEMANA 4 - INTELIGENCIA COMPUTACIONAL I")
print("=" * 70)


# -----------------------------------------------------------------------------
# 1. DEFINICAO DA TAREFA DE IC
# -----------------------------------------------------------------------------
# TAREFA: classificacao binaria supervisionada.
# Entrada : caracteristicas do par (usuario, filme).
# Alvo: gostou = 1 se rating >= 4, senao 0.
# Uso: a probabilidade prevista P(gostar) ranqueia os filmes a recomendar.


# -----------------------------------------------------------------------------
# 2. ESCOLHA JUSTIFICADA DA TECNICA
# -----------------------------------------------------------------------------
# TECNICAS:
# Rede Neural MLP: aprende interacoes NAO-LINEARES entre o perfil do
# usuario (comportamento + pesos + preferencia de genero), os generos e a
# qualidade (nota bayesiana) do filme, e o cluster de preferencia.
# (b) Algoritmo Genetico: otimiza um PESO POR GENERO numa similaridade de
# conteudo usuario-filme, maximizando a separacao entre gostou/nao-gostou
# (cumpre 'otimizar pesos de generos'). E interpretavel e nao usa gradiente.


# -----------------------------------------------------------------------------
# 3. PREPARACAO DOS DADOS (pares usuario-filme + features)
# -----------------------------------------------------------------------------
print("=" * 70)
print("3. PREPARACAO DOS DADOS")
print("=" * 70)

pares = avaliacoes[["userId", "movieId", "rating"]].copy()
if len(pares) > MAX_PARES:
    pares = pares.sample(MAX_PARES, random_state=RANDOM_STATE)
pares["gostou"] = (pares["rating"] >= LIMIAR_GOSTOU).astype(np.int8)
print(f"Pares (usuario,filme): {len(pares)} | % gostou: {pares['gostou'].mean():.1%}")

# (a) Preferencia de genero do USUARIO (gu_*) -> ja temos em generos_pct
gu = generos_pct.add_prefix("gu_")                       # index userId

# (b) One-hot de genero do FILME (gm_*) - reindex p/ todo filme (0 onde faltar)
fg2 = filmes[["movieId", "genres_list"]].explode("genres_list").reset_index(drop=True)
fg2 = fg2[fg2["genres_list"].isin(set(generos))]
filme_oh = pd.crosstab(fg2["movieId"], fg2["genres_list"])
filme_oh = filme_oh.reindex(index=filmes["movieId"].unique(),
                            columns=generos, fill_value=0)
filme_oh = (filme_oh > 0).astype(np.float32).add_prefix("gm_")   # index movieId

# (c) Comportamento + pesos do USUARIO ; qualidade do FILME
u_beh = perfil_usuario[["media_nota", "desvio_nota", "qtd_avaliacoes"]]
u_w = pesos_usuario
m_q = (filmes.set_index("movieId")[["nota_bayesiana", "qtd_avaliacoes"]]
       .rename(columns={"qtd_avaliacoes": "popularidade_filme"}))

# (d) Junta tudo nos pares (join por chave -> alinha por index)
X_df = (pares
        .join(u_beh, on="userId")
        .join(u_w, on="userId")
        .join(gu, on="userId")
        .join(m_q, on="movieId")
        .join(filme_oh, on="movieId"))
X_df["cluster_final"] = pares["userId"].map(perfil_usuario["cluster_final"])
_antes = len(X_df)
X_df = X_df.dropna().reset_index(drop=True)
print(f"Pares descartados por dados ausentes: {_antes - len(X_df)} "
      f"({(_antes - len(X_df)) / max(_antes, 1):.1%})")

# Blocos de colunas de genero (ordem identica = a lista 'generos' -> alinhamento)
col_gu = [f"gu_{g}" for g in generos]
col_gm = [f"gm_{g}" for g in generos]

# Similaridade de conteudo (match de genero usuario x filme) como feature extra
match = (X_df[col_gu].to_numpy() * X_df[col_gm].to_numpy()).sum(axis=1)
X_df["match_genero"] = match

# media_nota do usuario foi DELIBERADAMENTE excluida das features: ela e quase
# o proprio alvo (a media das notas do usuario ~ a taxa de "gostou" dele) e
# mascararia o efeito do cluster na comparacao do item 7.
col_base = (["desvio_nota", "qtd_avaliacoes",
             "peso_tags", "peso_notas", "peso_notas_boas",
             "nota_bayesiana", "popularidade_filme", "match_genero"]
            + col_gu + col_gm)

# Cluster one-hot (entra SO na versao "com clusters")
cluster_oh = pd.get_dummies(X_df["cluster_final"].astype(int),
                            prefix="cluster").astype(np.float32)

y = X_df["gostou"].to_numpy()
X_base = X_df[col_base].to_numpy(dtype=np.float32)
X_com = np.hstack([X_base, cluster_oh.to_numpy()])

# Split UNICO (mesmas linhas nas duas versoes -> comparacao com/sem cluster justa)
idx_tr, idx_te = train_test_split(
    np.arange(len(y)), test_size=0.25, random_state=RANDOM_STATE, stratify=y)
print(f"Treino: {len(idx_tr)} | Teste: {len(idx_te)} | "
      f"features base: {X_base.shape[1]} | com cluster: {X_com.shape[1]}")
print("LIMITACAO (vazamento de alvo): as features de perfil (pesos, nota "
      "bayesiana, generos) sao agregadas sobre TODA a base e o split ocorre "
      "depois -> o AUC do MLP abaixo e OTIMISTA. A media_nota do usuario (quase "
      "o alvo) foi removida; na versao final, recalcular as features so no treino.")


# -----------------------------------------------------------------------------
# 4-5 (a). CONSTRUCAO E TREINAMENTO DA REDE NEURAL (com e sem clusters)
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("4-5 (a). REDE NEURAL MLP - CONSTRUCAO E TREINAMENTO")
print("=" * 70)

def treinar_mlp(Xmat, nome):
    # Padronizacao ajustada SO no treino (evita vazamento do teste)
    sc = StandardScaler().fit(Xmat[idx_tr])
    Xtr, Xte = sc.transform(Xmat[idx_tr]), sc.transform(Xmat[idx_te])
    clf = MLPClassifier(hidden_layer_sizes=(32, 16), activation="relu",
                        max_iter=300, early_stopping=True,
                        random_state=RANDOM_STATE)
    clf.fit(Xtr, y[idx_tr])
    proba = clf.predict_proba(Xte)[:, 1]
    pred = (proba >= 0.5).astype(int)
    auc = roc_auc_score(y[idx_te], proba)
    print(f"  [{nome:18s}] AUC={auc:.3f} | acc={accuracy_score(y[idx_te], pred):.3f} "
          f"| F1={f1_score(y[idx_te], pred):.3f}")
    return auc

# Baseline ingenuo (referencia): sempre a classe majoritaria
dummy = DummyClassifier(strategy="most_frequent").fit(X_base[idx_tr], y[idx_tr])
print(f"  [baseline majoritaria] acc={accuracy_score(y[idx_te], dummy.predict(X_base[idx_te])):.3f}")

auc_sem = treinar_mlp(X_base, "MLP sem clusters")
auc_com = treinar_mlp(X_com, "MLP com clusters")


# -----------------------------------------------------------------------------
# 4-5 (b). ALGORITMO GENETICO: pesos por genero numa similaridade de conteudo
# -----------------------------------------------------------------------------
# score(par) = sum_g w_g * gu_g * gm_g   (similaridade de genero ponderada)
# fitness    = AUC(score, gostou) no TREINO ; avaliacao final no TESTE.
# Compara o w otimizado pelo GA contra o w uniforme (todos = 1).
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("4-5 (b). ALGORITMO GENETICO - pesos de genero")
print("=" * 70)

P = X_df[col_gu].to_numpy(np.float64) * X_df[col_gm].to_numpy(np.float64)
P_tr, P_te = P[idx_tr], P[idx_te]
y_tr, y_te = y[idx_tr], y[idx_te]
G = P.shape[1]

def fitness(w):
    s = P_tr @ w
    if np.ptp(s) == 0:          # score constante -> nao separa nada
        return 0.5
    return roc_auc_score(y_tr, s)

rng_ga = np.random.RandomState(RANDOM_STATE)
POP, GERACOES, ELITE, P_MUT = 40, 50, 4, 0.25
# Pesos COM SINAL em [-1, 1]: +g puxa o score para "gostar", -g para "nao
# gostar". Com pesos so positivos (e P>=0) o GA nao conseguiria corrigir
# generos anti-informativos e poderia ficar preso em AUC < 0.5.
pop = rng_ga.uniform(-1.0, 1.0, size=(POP, G))
best_w, best_fit, historico = None, -1.0, []

for ger in range(GERACOES):
    fits = np.array([fitness(w) for w in pop])
    if fits.max() > best_fit:
        best_fit = float(fits.max())
        best_w = pop[int(fits.argmax())].copy()
    historico.append(best_fit)

    ordem = np.argsort(-fits)
    pop, fits = pop[ordem], fits[ordem]
    nova = [pop[i].copy() for i in range(ELITE)]          # elitismo
    while len(nova) < POP:
        # selecao por torneio (2 vs 2), sem reposicao (evita comparar com si mesmo)
        i, j = rng_ga.choice(POP, size=2, replace=False)
        pai = pop[i] if fits[i] >= fits[j] else pop[j]
        k, l = rng_ga.choice(POP, size=2, replace=False)
        mae = pop[k] if fits[k] >= fits[l] else pop[l]
        # crossover uniforme
        mask = rng_ga.rand(G) < 0.5
        filho = np.where(mask, pai, mae)
        # mutacao gaussiana
        if rng_ga.rand() < P_MUT:
            filho = np.clip(filho + rng_ga.normal(0, 0.1, G), -1.0, 1.0)
        nova.append(filho)
    pop = np.array(nova)

# Avaliacao no TESTE: GA vs uniforme
auc_ga   = roc_auc_score(y_te, P_te @ best_w)
auc_unif = roc_auc_score(y_te, P_te @ np.ones(G))
print(f"AUC (teste) similaridade GA      : {auc_ga:.3f}")
print(f"AUC (teste) similaridade uniforme: {auc_unif:.3f}")
print(f"Ganho do GA sobre o uniforme     : {auc_ga - auc_unif:+.3f}")
if auc_ga < 0.5:
    print("  AVISO: AUC < 0.5 no teste -> score anti-correlacionado (overfit do "
          "fitness na base pequena); tende a melhorar com mais dados.")
if auc_ga <= auc_unif:
    print("  Obs.: o GA nao superou o uniforme nesta base pequena - esperado "
          "melhorar na base ~300x / com mais geracoes.")

pesos_genero = pd.Series(best_w, index=generos).sort_values(ascending=False)
print("\nPesos de genero aprendidos (+ puxa p/ gostar, - p/ nao gostar):")
print(pd.concat([pesos_genero.head(5), pesos_genero.tail(5)]).round(3).to_string())

# Curva de evolucao do GA
plt.figure(figsize=(8, 4))
plt.plot(range(1, GERACOES + 1), historico, marker=".")
plt.xlabel("Geracao")
plt.ylabel("Melhor fitness (AUC treino)")
plt.title("Evolucao do Algoritmo Genetico")
plt.grid(True)
plt.tight_layout()
plt.savefig(os.path.join(DIR_IMAGENS, "ga_evolucao.png"), dpi=100)
plt.close()
print("Grafico salvo: ga_evolucao.png")


# -----------------------------------------------------------------------------
# AVALIACAO PRELIMINAR CONSOLIDADA + SAIDA
# -----------------------------------------------------------------------------
print("\n" + "=" * 70)
print("6. AVALIACAO PRELIMINAR (consolidada)")
print("=" * 70)
print(f"Prevalencia da classe 'gostou' = {y.mean():.1%} | referencia de AUC = 0.5")
resultado = pd.DataFrame([
    {"modelo": "MLP sem clusters",        "auc_teste": round(auc_sem, 3)},
    {"modelo": "MLP com clusters",        "auc_teste": round(auc_com, 3)},
    {"modelo": "Similaridade GA",         "auc_teste": round(auc_ga, 3)},
    {"modelo": "Similaridade uniforme",   "auc_teste": round(auc_unif, 3)},
])
print(resultado.to_string(index=False))

print("\n" + "=" * 70)
print("7. COMPARACAO COM E SEM CLUSTERS")
print("=" * 70)
delta = auc_com - auc_sem
print(f"MLP sem clusters : AUC {auc_sem:.3f}")
print(f"MLP com clusters : AUC {auc_com:.3f}")
print(f"Efeito de adicionar o cluster (feature da Semana 3): {delta:+.3f}")
print("  -> o cluster " + ("AJUDOU" if delta > 0.005 else
      "PIOROU" if delta < -0.005 else "foi NEUTRO") +
      " na previsao de 'vai gostar'.")

resultado.to_csv(os.path.join(DIR_CSVS, "resultado_ic.csv"), index=False)
pesos_genero.rename("peso_ga").to_csv(os.path.join(DIR_CSVS, "pesos_generos_ga.csv"))
print("\n-> resultado_ic.csv | pesos_generos_ga.csv | ga_evolucao.png")