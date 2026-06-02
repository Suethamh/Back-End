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