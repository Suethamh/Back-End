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
print(f"\nQuantidade de avaliações fora do intervalo esperado: {len(fora_intervalo)}")

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
from sklearn.metrics import silhouette_score
import matplotlib.pyplot as plt


# 1. SELECAO DOS ATRIBUTOS PARA AGRUPAMENTO
print("1. SELECAO DOS ATRIBUTOS PARA AGRUPAMENTO")

# Atributos COMPORTAMENTAIS do usuario
perfil = avaliacoes.groupby("userId").agg(
    media_nota=("rating", "mean"),
    desvio_nota=("rating", "std"),
    qtd_avaliacoes=("rating", "size"),
)
perfil["desvio_nota"] = perfil["desvio_nota"].fillna(0)

# Atributos de PREFERENCIA por genero (% das avaliacoes do usuario em cada genero)
av_com_generos = (avaliacoes.merge(filmes[["movieId", "genres_list"]],
                                   on="movieId")
                            .explode("genres_list"))

generos_pct = (av_com_generos.groupby(["userId", "genres_list"]).size()
               .unstack(fill_value=0))
generos_pct = generos_pct.div(generos_pct.sum(axis=1), axis=0)

# Combina os dois grupos de atributos em um unico vetor por usuario
perfil_usuario = perfil.join(generos_pct, how="left").fillna(0)

print("Atributos selecionados para cada usuario:")
print(perfil_usuario.head())
print(f"\nTotal de usuarios: {perfil_usuario.shape[0]}")
print(f"Total de atributos por usuario: {perfil_usuario.shape[1]}")

# 2. JUSTIFICATIVA DOS ATRIBUTOS ESCOLHIDOS
#  media_nota: identifica usuarios 'generosos' x 'criticos'.
#  desvio_nota: identifica usuarios 'estaveis' x 'ecleticos'.
#  qtd_avaliacoes: separa usuarios casuais dos engajados.
#  % de cada genero: representa a PREFERENCIA do usuario por conteudo,
#                     conectando o agrupamento ao tema central do projeto
#                     (recomendacao de filmes).

# Combinamos atributos COMPORTAMENTAIS (como o usuario avalia) com
# atributos de CONTEUDO (o que ele gosta de assistir), formando um
# vetor de preferencias

# 3. NORMALIZACAO / PADRONIZACAO
# Justificativa: 'qtd_avaliacoes' pode chegar a 300+ enquanto 
# 'media_nota'vai ate 5 e os '%generos' vao ate 1. Sem padronizar, qtd_avaliacoes dominaria a distancia euclidiana. 
# StandardScaler deixa todas as colunas com media 0 e desvio 1.
scaler = StandardScaler()
X = scaler.fit_transform(perfil_usuario)

print("Estatisticas APOS padronizacao (media ~ 0, desvio ~ 1):")
print(pd.DataFrame(X, columns=perfil_usuario.columns)
      .describe().round(2).loc[["mean", "std"]])

# 4. MEDIDA DE DISTANCIA / SIMILARIDADE
# Medida adotada: DISTANCIA EUCLIDIANA
# Justificativa:
# - E a metrica padrao do K-Means.
# - Apropriada para atributos numericos continuos.
# - Funciona bem apos a padronizacao (todas as dimensoes na mesma escala).

# 5. APLICACAO DO PRIMEIRO ALGORITMO DE AGRUPAMENTO (K-MEANS)
inercias    = []
silhouettes = []
ks = range(2, 11)
for k in ks:
    km = KMeans(n_clusters=k, random_state=42, n_init=10)
    rotulos = km.fit_predict(X)
    inercias.append(km.inertia_)
    silhouettes.append(silhouette_score(X, rotulos))
    print(f"  k={k} | inercia={km.inertia_:8.2f} | "
          f"silhouette={silhouettes[-1]:.3f}")

K_IDEAL = 4   # ajuste apos analisar o grafico do cotovelo
print(f"\nValor de k escolhido: {K_IDEAL}")

kmeans = KMeans(n_clusters=K_IDEAL, random_state=42, n_init=10)
perfil_usuario["cluster"] = kmeans.fit_predict(X)

# 6. VISUALIZACAO DOS DADOS
# Reduz para 2D (apenas para conseguir plotar) usando PCA
X_2d = PCA(n_components=2, random_state=42).fit_transform(X)

fig, axes = plt.subplots(1, 2, figsize=(14, 5))

# Grafico A: metodo do cotovelo
axes[0].plot(list(ks), inercias, marker="o")
axes[0].set_xlabel("Numero de clusters (k)")
axes[0].set_ylabel("Inercia")
axes[0].set_title("Metodo do Cotovelo")
axes[0].grid(True)

# Grafico B: clusters em 2D
sc = axes[1].scatter(X_2d[:, 0], X_2d[:, 1],
                     c=perfil_usuario["cluster"], cmap="tab10", s=30)
axes[1].set_xlabel("Componente Principal 1")
axes[1].set_ylabel("Componente Principal 2")
axes[1].set_title(f"Clusters de Usuarios (k={K_IDEAL})")
plt.colorbar(sc, ax=axes[1], label="Cluster")

plt.tight_layout()
plt.savefig("clusters_usuarios.png", dpi=100)
plt.show()
print("Grafico salvo em: clusters_usuarios.png")

# 7. ANALISE INICIAL DOS CLUSTERS

print("\nTamanho de cada cluster:")
print(perfil_usuario["cluster"].value_counts().sort_index())

print("\nMedia dos atributos COMPORTAMENTAIS em cada cluster:")
print(perfil_usuario.groupby("cluster")[
    ["media_nota", "desvio_nota", "qtd_avaliacoes"]
].mean().round(2))

print("\nTOP 3 generos preferidos em cada cluster:")
colunas_genero = generos_pct.columns
for c in sorted(perfil_usuario["cluster"].unique()):
    medias_genero = (perfil_usuario[perfil_usuario["cluster"] == c]
                     [colunas_genero].mean()
                     .sort_values(ascending=False).head(3))
    print(f"\nCluster {c}:")
    print(medias_genero.round(3).to_string())


# 8. IDENTIFICACAO DE PROBLEMAS NO AGRUPAMENTO INICIAL
tamanhos = perfil_usuario["cluster"].value_counts()
razao = tamanhos.max() / tamanhos.min()
sil_final = silhouette_score(X, perfil_usuario["cluster"])

print(f"\n[1] Balanceamento dos clusters")
print(f"Maior cluster: {tamanhos.max()} usuarios")
print(f"Menor cluster: {tamanhos.min()} usuarios")
print(f"Razao maior/menor: {razao:.2f}x")
if razao > 5:
    print("PROBLEMA: clusters muito desbalanceados.")

print(f"\n[2] Qualidade do agrupamento")
print(f"Silhouette Score final (k={K_IDEAL}): {sil_final:.3f}")
if sil_final < 0.25:
    print("PROBLEMA: silhouette baixo -> clusters pouco definidos.")
elif sil_final < 0.5:
    print("ATENCAO: silhouette razoavel, com sobreposicao parcial.")
else:
    print("OK: silhouette indica clusters bem separados.")

print("\n[3] Limitacoes do K-Means observadas")
print("- Assume clusters esfericos e de tamanho similar.")
print("- Sensivel a outliers (usuarios extremos distorcem o centroide).")
print("- O valor de k foi escolhido visualmente (nao garante otimo).")
print("- Inicializacao aleatoria afeta resultado (mitigado com n_init=10).")
