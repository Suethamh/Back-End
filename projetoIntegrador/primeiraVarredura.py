# Tratamento de valores ausentes.
# Tratamento de duplicidades.
# Identificação preliminar de outliers.
# Transformação inicial de atributos categóricos, quando necessário.
# Primeira análise exploratória.

import pandas as pd
import numpy as np

pd.set_option('display.max_columns', None)
pd.set_option('display.width', 200)

PASTA = "ml-latest-small/"

filmes = pd.read_csv(PASTA + "movies.csv")
avaliacoes = pd.read_csv(PASTA + "ratings.csv")
tags = pd.read_csv(PASTA + "tags.csv")
links = pd.read_csv(PASTA + "links.csv")

dataset = {
    "filmes": filmes,
    "avaliacoes": avaliacoes,
    "tags": tags,
    "links": links
}

print("\nVisão do dataset:")
for nome, df in dataset.items():
    print(f"\n--- {nome.upper()} ---")
    print(f"Dimensão: {df.shape}")
    print(f"\nTipo de Dados:")
    print(df.dtypes)
    print(f"\nValores exemplo:")
    print(df.head())


print("\nAnálise de valores ausentes:")

for nome, df in dataset.items():
    ausentes = df.isnull().sum()
    total = ausentes.sum()
    print(f"\n--- {nome.upper()} ---")
    print(f"Total de valores ausentes: {total}")

print("\nRemovendo valores ausentes...")
for nome, df in [("filmes", filmes), ("avaliacoes", avaliacoes), ("tags", tags), ("links", links)]:
    antes = len(df)
    df.dropna(inplace=True)
    depois = len(df)
    print(f"{nome} - antes: {antes} linhas, depois: {depois} linhas, removidos: {antes - depois} linhas")


print("\nAnálise de duplicidades:")

for nome, df in dataset.items():
    qtd_duplicados = df.duplicated().sum()
    print(f"{nome} - linhas totalmente duplicadas: {qtd_duplicados}")
    
filmes.drop_duplicates(subset=["movieId"], inplace=True)
avaliacoes.drop_duplicates(subset=["userId", "movieId"], inplace=True)
tags.drop_duplicates(subset=["userId", "movieId", "tag"], inplace=True)
links.drop_duplicates(subset=["movieId"], inplace=True)


print("\nAnálise de outliers (avaliações):")
print(avaliacoes["rating"].describe())
fora_intervalo = avaliacoes[(avaliacoes["rating"] < 0.5) | (avaliacoes["rating"] > 5)]
print(f"\nQtd de avaliações por filme fora do intervalo esperado: {len(fora_intervalo)}")

notas_filme = avaliacoes.groupby("movieId").size()
print(notas_filme.describe())
filmes_pouco_avaliados = notas_filme[notas_filme <= 5]
print(f"\nQuantidade de filmes com menos de 5 avaliações: {len(filmes_pouco_avaliados)}")

print("\nRemovendo os outliars...")
antes = len(avaliacoes)
avaliacoes = avaliacoes[(avaliacoes["rating"] >= 0.5) & (avaliacoes["rating"] <= 5)]
print(f"notas fora do intervalo - antes: {antes} linhas, depois: {len(avaliacoes)} linhas, removidos: {antes - len(avaliacoes)} linhas")

notas_filme = avaliacoes.groupby("movieId").size()
filmes_validos = notas_filme[notas_filme > 5].index

antes_fl = len(filmes)
filmes = filmes[filmes["movieId"].isin(filmes_validos)]
print(f"filmes: {antes_fl} -> {len(filmes)} "
      f"({antes_fl - len(filmes)} filmes removidos)")

print("\nTransformação de atributos categóricos (tags):")
filmes["genres_list"] = filmes["genres"].str.split("|")
print(filmes[["title", "genres", "genres_list"]].head())

avaliacoes["datetime"] = pd.to_datetime(avaliacoes["timestamp"], unit="s")
tags["datetime"] = pd.to_datetime(tags["timestamp"], unit="s")
print("\nExemplo de timestamps convertidos em ratings:")
print(avaliacoes[["userId", "movieId", "rating", "datetime"]].head())


print("\nPrimeira análise exploratória:")
num_usuarios = avaliacoes["userId"].nunique()
num_filmes = avaliacoes["movieId"].nunique()
num_avaliacoes = len(avaliacoes)
print(f"Número de usuários únicos: {num_usuarios}")
print(f"Número de filmes únicos: {num_filmes}")
print(f"Número total de avaliações: {num_avaliacoes}")

print("\nTOP 10 Gêneros mais frequentes")
top_generos = filmes.explode("genres_list")
print(top_generos["genres_list"].value_counts().head(10))

print("\nTOP 10 filmes mais avaliados:")
top_filmes = (avaliacoes.groupby("movieId").size().sort_values(ascending=False).head(10).reset_index(name="qtd_avaliacoes").merge(filmes[["movieId", "title"]], on="movieId"))
print(top_filmes[["title", "qtd_avaliacoes"]])

print("\nTOP 10 filmes com melhor avaliação média (50 avaliações):")
status_filme = avaliacoes.groupby("movieId").agg(qtd_avaliacoes=("rating", "size"), media_avaliacao=("rating", "mean")).reset_index()
top_qualidade = status_filme[status_filme["qtd_avaliacoes"] >= 50].sort_values("media_avaliacao", ascending=False).head(10).merge(filmes[["movieId", "title"]], on="movieId")
print(top_qualidade[["title", "media_avaliacao", "qtd_avaliacoes"]])

# Seleção dos atributos para agrupamento.
# Justificativa dos atributos escolhidos.
# Normalização ou padronização.
# Definição da medida de distância ou similaridade.
# Aplicação do primeiro algoritmo de agrupamento.
# Aplicação de pelo menos uma visualização dos dados.
# Análise inicial dos clusters.
# Identificação de problemas no agrupamento inicial.


from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.metrics import davies_bouldin_score
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

# ============================================================================
# SELEÇÃO DE ATRIBUTOS PARA AGRUPAMENTO
# ============================================================================
print("\n" + "="*70)
print("ATRIBUTOS SELECIONADOS PARA AGRUPAMENTO DE USUÁRIOS")
print("="*70)

# Feature Engineering: Atributos comportamentais do usuario
# Calcula média, desvio e quantidade de avaliações por usuário
perfil = avaliacoes.groupby("userId").agg(
    media_nota=("rating", "mean"),
    desvio_nota=("rating", "std"),
    qtd_avaliacoes=("rating", "size"),
)
perfil["desvio_nota"] = perfil["desvio_nota"].fillna(0)

# Feature Engineering: Preferencia por genero
# Expande gêneros e calcula proporção de avaliações por gênero por usuário
filmes["genres_list"] = filmes["genres"].str.split("|")
av_com_generos = (avaliacoes.merge(filmes[["movieId", "genres_list"]],
                    on="movieId")
                .explode("genres_list"))

generos_pct = (av_com_generos.groupby(["userId", "genres_list"]).size()
               .unstack(fill_value=0))
generos_pct = generos_pct.div(generos_pct.sum(axis=1), axis=0)

# Combine attributes
# Junta comportamento e preferências de gênero em um único vetor por usuário
perfil_usuario = perfil.join(generos_pct, how="left").fillna(0)

# Descrição dos atributos
print(f"Users: {perfil_usuario.shape[0]}, features: {perfil_usuario.shape[1]}")


# ============================================================================
# NORMALIZAÇÃO - StandardScaler (z-score)
# ============================================================================
print("="*70)
print("NORMALIZAÇÃO DOS DADOS")
print("="*70)

scaler = StandardScaler()
X = scaler.fit_transform(perfil_usuario)

# Normalização: StandardScaler (Z-score) aplicada
# (saída reduzida para ser concisa)
stats = pd.DataFrame(X, columns=perfil_usuario.columns).describe().loc[["mean", "std"]]
print(f"Media/desvio (exemplos) after scaling: media_nota={stats.at['mean','media_nota']:.3f}, std={stats.at['std','media_nota']:.3f}")

# ============================================================================
# AGRUPAMENTO - K-Means
# Agrupa usuários por similaridade no espaço padronizado de features
# ============================================================================
# Avaliação K=2..10 (sem impressões detalhadas)# Testa diferentes valores de K e calcula Davies-Bouldin para cada um

db_indices = []
ks = range(2, 11)
for k in ks:
        km = KMeans(n_clusters=k, random_state=42, n_init=10)
        rotulos = km.fit_predict(X)
        db = davies_bouldin_score(X, rotulos)
        db_indices.append(db)

# Clustering final com k=4
K_IDEAL = 4
kmeans = KMeans(n_clusters=K_IDEAL, random_state=42, n_init=10)
perfil_usuario["cluster"] = kmeans.fit_predict(X)
print(f"K escolhido: {K_IDEAL}")

# PCA 2D para visualizacao (projecao dos 27D em 2D)
pca = PCA(n_components=2, random_state=42)
X_2d = pca.fit_transform(X)
var_pc1 = pca.explained_variance_ratio_[0] * 100
var_pc2 = pca.explained_variance_ratio_[1] * 100

plt.figure(figsize=(10, 7))
plt.scatter(X_2d[:, 0], X_2d[:, 1],
            c=perfil_usuario["cluster"], cmap="tab10", s=50, alpha=0.7)
plt.xlabel(f"Componente Principal 1 ({var_pc1:.1f}% da informacao)", fontsize=12)
plt.ylabel(f"Componente Principal 2 ({var_pc2:.1f}% da informacao)", fontsize=12)
plt.title(f"Clusters de Usuarios (k={K_IDEAL})", fontsize=14)
plt.colorbar(label="Cluster")
# Salva grafico em arquivo
plt.savefig("clusters_usuarios.png", dpi=100)
plt.close()
print("Grafico salvo em: clusters_usuarios.png")

# ============================================================================
# ANÁLISE E INTERPRETAÇÃO DOS CLUSTERS
# ============================================================================
print("\n" + "="*70)
print("ANÁLISE E INTERPRETAÇÃO DOS CLUSTERS")
print("="*70)

print("\nDistribuicao dos usuarios por cluster:")
tamanhos = perfil_usuario["cluster"].value_counts().sort_index()
for c, size in tamanhos.items():
    pct = (size / len(perfil_usuario)) * 100
    print(f"  Cluster {c}: {size:5d} usuários ({pct:5.1f}%)")

# Media dos 3 atributos comportamentais por cluster
print("\nCaracteristicas comportamentais por cluster:")
comportamento = perfil_usuario.groupby("cluster")[
    ["media_nota", "desvio_nota", "qtd_avaliacoes"]
].mean()
print(comportamento.round(2).to_string())

# Top 5 generos por cluster
print("\nPreferencias de genero por cluster:")
colunas_genero = generos_pct.columns
for c in sorted(perfil_usuario["cluster"].unique()):
    medias_genero = (perfil_usuario[perfil_usuario["cluster"] == c]
                     [colunas_genero].mean()
                     .sort_values(ascending=False).head(5))
    print(f"\nCluster {c} - TOP 5 gêneros:")
    for gen, val in medias_genero.items():
        print(f"  {gen:15s}: {val:.1%}")

# Calcula metricas de qualidade (balanceamento e separacao)
tamanhos = perfil_usuario["cluster"].value_counts()
razao = tamanhos.max() / tamanhos.min()
db_final = davies_bouldin_score(X, perfil_usuario["cluster"])

# Avalia qualidade do agrupamento
print("\nQUALIDADE DO AGRUPAMENTO")

print(f"Balanceamento:")
print(f"  Maior cluster: {tamanhos.max()} usuarios")
print(f"  Menor cluster: {tamanhos.min()} usuarios")
print(f"  Razao maior/menor: {razao:.2f}x")
# Davies-Bouldin: quanto menor, melhor (< 1.5 eh bom)
print(f"Davies-Bouldin Index final (k={K_IDEAL}): {db_final:.3f}")

# Aplicação de um segundo algoritmo de agrupamento.
# Comparação entre os algoritmos.
# Uso de pelo menos duas métricas de avaliação.
# Análise dos parâmetros utilizados.
# Interpretação dos clusters.
# Nomeação dos perfis encontrados.
# Definição de como os clusters serão usados na Inteligência Computacional.
# Geração da variável ou estrutura de saída do agrupamento.
# Produto esperado: Etapa de Agrupamento de Dados consolidada, com clusters interpretados e prontos para alimentar a
# etapa de Inteligência Computacional.

from sklearn.cluster import AgglomerativeClustering
from sklearn.metrics import (silhouette_score, davies_bouldin_score,
                             calinski_harabasz_score, adjusted_rand_score)
from scipy.cluster.hierarchy import dendrogram, linkage

# Guarda o resultado do K-Means (Semana 2) para nao sobrescrever
perfil_usuario["cluster_kmeans"] = perfil_usuario["cluster"]


# -----------------------------------------------------------------------------
# 1. APLICACAO DE UM SEGUNDO ALGORITMO DE AGRUPAMENTO (HIERARQUICO)
# -----------------------------------------------------------------------------

print("\n\n1. SEGUNDO ALGORITMO: AGRUPAMENTO HIERARQUICO")

# Usamos o MESMO numero de clusters do K-Means para uma comparacao justa.
hier = AgglomerativeClustering(n_clusters=K_IDEAL, linkage="ward")
perfil_usuario["cluster_hier"] = hier.fit_predict(X)

print(f"Algoritmo aplicado com k={K_IDEAL} e linkage='ward'.")
print("Distribuicao dos usuarios (Hierarquico):")
print(perfil_usuario["cluster_hier"].value_counts().sort_index().to_string())


# -----------------------------------------------------------------------------
# 4. ANALISE DOS PARAMETROS UTILIZADOS
#    (colocada aqui porque justifica as escolhas de cada algoritmo)
# -----------------------------------------------------------------------------
print("\n4. ANALISE DOS PARAMETROS")

# 4.1 Escolha do linkage no Hierarquico (testamos 3 opcoes)
print("\n[Hierarquico] Comparacao de metodos de linkage (k fixo):")
for metodo in ["ward", "complete", "average"]:
    rot = AgglomerativeClustering(n_clusters=K_IDEAL, linkage=metodo).fit_predict(X)
    sil = silhouette_score(X, rot)
    db  = davies_bouldin_score(X, rot)
    print(f"  linkage={metodo:9s} | silhouette={sil:.3f} | davies_bouldin={db:.3f}")
print("Escolhido 'ward': minimiza a variancia interna dos clusters.")

# 4.2 Reavaliacao do k para os DOIS algoritmos (justifica k escolhido)
print(f"\n[Ambos] Metricas variando k (justificativa do k={K_IDEAL}):")
print(f"  {'k':>2} | {'KMeans_sil':>10} | {'Hier_sil':>9} | {'KMeans_DB':>9} | {'Hier_DB':>8}")
for k in range(2, 9):
    km_r = KMeans(n_clusters=k, random_state=42, n_init=10).fit_predict(X)
    hi_r = AgglomerativeClustering(n_clusters=k, linkage="ward").fit_predict(X)
    print(f"  {k:>2} | {silhouette_score(X, km_r):>10.3f} | "
          f"{silhouette_score(X, hi_r):>9.3f} | "
          f"{davies_bouldin_score(X, km_r):>9.3f} | "
          f"{davies_bouldin_score(X, hi_r):>8.3f}")

print("\nParametros finais documentados:")
print(f"K-Means - n_clusters={K_IDEAL}, init='k-means++', n_init=10, random_state=42")
print(f"Hierarquico - n_clusters={K_IDEAL}, linkage='ward', metric='euclidean'")


# -----------------------------------------------------------------------------
# 3. USO DE PELO MENOS DUAS METRICAS DE AVALIACAO
# -----------------------------------------------------------------------------
print("\n\n3. METRICAS DE AVALIACAO (3 metricas)")

def avaliar(X, labels):
    """Calcula 3 metricas internas de qualidade do agrupamento."""
    return {
        "silhouette": silhouette_score(X, labels), # maior = melhor (-1 a 1)
        "calinski_harabasz": calinski_harabasz_score(X, labels), # maior = melhor
        "davies_bouldin": davies_bouldin_score(X, labels), # menor = melhor
    }

metricas_kmeans = avaliar(X, perfil_usuario["cluster_kmeans"])
metricas_hier   = avaliar(X, perfil_usuario["cluster_hier"])

print(f"{'Metrica':>20} | {'K-Means':>10} | {'Hierarquico':>12} | melhor")
print("-" * 60)
for m in metricas_kmeans:
    v_km, v_hi = metricas_kmeans[m], metricas_hier[m]
    if m == "davies_bouldin":   # menor e melhor
        melhor = "K-Means" if v_km < v_hi else "Hierarquico"
    else:                        # maior e melhor
        melhor = "K-Means" if v_km > v_hi else "Hierarquico"
    print(f"{m:>20} | {v_km:>10.3f} | {v_hi:>12.3f} | {melhor}")


# -----------------------------------------------------------------------------
# 2. COMPARACAO ENTRE OS ALGORITMOS
# -----------------------------------------------------------------------------
print("\n\n2. COMPARACAO ENTRE OS ALGORITMOS")

# 2.1 Concordancia entre os dois agrupamentos
# ARI ~1 = agrupamentos quase iguais; ~0 = concordancia aleatoria
ari = adjusted_rand_score(perfil_usuario["cluster_kmeans"],
                          perfil_usuario["cluster_hier"])
print(f"\nAdjusted Rand Index (concordancia entre os 2): {ari:.3f}")
if ari > 0.7:
    print("Alta concordancia: os algoritmos encontram estrutura parecida.")
elif ari > 0.4:
    print("Concordancia moderada: estrutura parecida, mas com divergencias.")
else:
    print("Baixa concordancia: os algoritmos enxergam grupos diferentes.")

# 2.3 Visualizacao lado a lado (mesma projecao PCA, cores diferentes)
fig, axes = plt.subplots(1, 2, figsize=(15, 6))
axes[0].scatter(X_2d[:, 0], X_2d[:, 1],
                c=perfil_usuario["cluster_kmeans"], cmap="tab10", s=40, alpha=0.7)
axes[0].set_title("K-Means")
axes[1].scatter(X_2d[:, 0], X_2d[:, 1],
                c=perfil_usuario["cluster_hier"], cmap="tab10", s=40, alpha=0.7)
axes[1].set_title("Hierarquico (Ward)")
for ax in axes:
    ax.set_xlabel("Componente Principal 1")
    ax.set_ylabel("Componente Principal 2")
plt.suptitle("Comparacao dos Agrupamentos (projecao PCA 2D)")
plt.tight_layout()
plt.savefig("comparacao_algoritmos.png", dpi=100)
plt.close()
print("\nGrafico salvo em: comparacao_algoritmos.png")

# 2.4 Dendrograma (visualizacao exclusiva do hierarquico)
plt.figure(figsize=(12, 6))
Z = linkage(X, method="ward")
dendrogram(Z, truncate_mode="level", p=4)
plt.title("Dendrograma - Agrupamento Hierarquico (Ward)")
plt.xlabel("Usuarios (agrupados)")
plt.ylabel("Distancia")
plt.tight_layout()
plt.savefig("dendrograma.png", dpi=100)
plt.close()
print("Grafico salvo em: dendrograma.png")

# 2.5 ESCOLHA DO ALGORITMO FINAL (baseado na maioria das metricas)
votos_km = sum([metricas_kmeans["silhouette"] > metricas_hier["silhouette"],
                metricas_kmeans["calinski_harabasz"] > metricas_hier["calinski_harabasz"],
                metricas_kmeans["davies_bouldin"] < metricas_hier["davies_bouldin"]])
ALGO_FINAL = "K-Means" if votos_km >= 2 else "Hierarquico"
coluna_final = "cluster_kmeans" if ALGO_FINAL == "K-Means" else "cluster_hier"
perfil_usuario["cluster_final"] = perfil_usuario[coluna_final]
print(f"\nALGORITMO ESCOLHIDO: {ALGO_FINAL} "
      f"(venceu em {max(votos_km, 3 - votos_km)} de 3 metricas)")


# -----------------------------------------------------------------------------
# 5. INTERPRETACAO DOS CLUSTERS  +  6. NOMEACAO DOS PERFIS
# -----------------------------------------------------------------------------
print("\n5 e 6. INTERPRETACAO E NOMEACAO DOS PERFIS")
colunas_genero = generos_pct.columns
media_global = perfil_usuario["media_nota"].mean()
qtd_global   = perfil_usuario["qtd_avaliacoes"].mean()

def classificar_engajamento(qtd):
    if qtd > 1.3 * qtd_global: return "Engajado"
    if qtd < 0.7 * qtd_global: return "Casual"
    return "Moderado"

def classificar_tendencia(media):
    if media > media_global + 0.15: return "Generoso"
    if media < media_global - 0.15: return "Critico"
    return "Equilibrado"

nomes_perfis = {}
for c in sorted(perfil_usuario["cluster_final"].unique()):
    grupo = perfil_usuario[perfil_usuario["cluster_final"] == c]
    media_c = grupo["media_nota"].mean()
    qtd_c   = grupo["qtd_avaliacoes"].mean()
    genero_top = grupo[colunas_genero].mean().idxmax()

    eng  = classificar_engajamento(qtd_c)
    tend = classificar_tendencia(media_c)
    nome = f"{eng} {tend} - fa de {genero_top}"
    nomes_perfis[c] = nome

    print(f"\n--- CLUSTER {c}: \"{nome}\" ---")
    print(f"  Tamanho            : {len(grupo)} usuarios "
          f"({len(grupo)/len(perfil_usuario)*100:.1f}%)")
    print(f"  Media de nota      : {media_c:.2f} (global: {media_global:.2f})")
    print(f"  Avaliacoes/usuario : {qtd_c:.0f} (global: {qtd_global:.0f})")
    print(f"  Genero dominante   : {genero_top}")
    top3 = grupo[colunas_genero].mean().sort_values(ascending=False).head(3)
    print(f"  Top 3 generos      : "
          + ", ".join([f"{g} ({v:.1%})" for g, v in top3.items()]))

# Aplica o nome do perfil a cada usuario
perfil_usuario["perfil_nome"] = perfil_usuario["cluster_final"].map(nomes_perfis)


# -----------------------------------------------------------------------------
# 7. COMO OS CLUSTERS SERAO USADOS NA INTELIGENCIA COMPUTACIONAL
# -----------------------------------------------------------------------------
# (a) ALVO DE CLASSIFICACAO:
#     Um modelo de IC aprende a prever o cluster de um usuario NOVO a partir das primeiras avaliacoes dele.
#     Assim, mesmo sem historico, o usuario ja entra em um grupo de preferencia.

# (b) FILTRO PARA RECOMENDACAO:
#     Conhecido o cluster do usuario, recomendam-se os filmes mais bem avaliados
#     DENTRO daquele cluster (filtragem colaborativa baseada em grupos).

# (c) ATRIBUTO DE ENTRADA (feature):
#     O 'perfil_nome' / cluster vira uma variavel categorica de entrada para
#     modelos futuros de previsao de nota ou ranking de filmes.


# -----------------------------------------------------------------------------
# 8. GERACAO DA ESTRUTURA DE SAIDA DO AGRUPAMENTO
# -----------------------------------------------------------------------------
print("\n8. ESTRUTURA DE SAIDA (arquivos que alimentam a IC)")

# 8.1 Saida principal: cada usuario com seu cluster e perfil
saida_usuarios = (perfil_usuario.reset_index()
                  [["userId", "media_nota", "desvio_nota", "qtd_avaliacoes",
                    "cluster_final", "perfil_nome"]])
saida_usuarios.to_csv("usuarios_clusterizados.csv", index=False)
print(f"usuarios_clusterizados.csv ({len(saida_usuarios)} usuarios)")

# 8.2 Resumo dos perfis (1 linha por cluster)
resumo_perfis = (perfil_usuario.groupby(["cluster_final", "perfil_nome"])
                 .agg(qtd_usuarios=("media_nota", "size"),
                      media_nota=("media_nota", "mean"),
                      media_avaliacoes=("qtd_avaliacoes", "mean"))
                 .round(2).reset_index())
resumo_perfis.to_csv("perfis_clusters.csv", index=False)
print(f"perfis_clusters.csv({len(resumo_perfis)} perfis)")

print("\nPreview da saida principal:")
print(saida_usuarios.head().to_string(index=False))
