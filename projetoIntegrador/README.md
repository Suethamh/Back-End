# Projeto Integrador — Sistema de Recomendação de Filmes (MovieLens)

## Dataset

Baixe o MovieLens em: https://grouplens.org/datasets/movielens/

- **Base pequena** (teste rápido, ~100 mil avaliações):
  https://files.grouplens.org/datasets/movielens/ml-latest-small.zip
- **Base completa** (~33 milhões de avaliações, ~300 MB):
  https://files.grouplens.org/datasets/movielens/ml-latest.zip

### Onde colocar
Descompacte e coloque as pastas na **raiz do projeto** (ao lado de `codigo/`),
exatamente com estes nomes:

```
projetoIntegrador/
├── ml-latest-small/   <- movies.csv, ratings.csv, tags.csv, links.csv
├── ml-latest/         <- mesma estrutura (base completa; ignorada pelo git)
├── codigo/
├── imagens/
└── csvs/
```

Não é preciso editar caminho nenhum no código: os scripts localizam a raiz do
projeto sozinhos. Ao rodar, o código **pergunta qual base usar** (digite `1`
para a pequena ou `2` para a completa).

## Como rodar

No terminal, a partir da raiz do projeto:

```
python codigo/primeiraVarredura.py
```

(rode no terminal, pois os scripts pedem para você digitar `1` ou `2`)

## Estrutura

- `codigo/`  — scripts Python
- `imagens/` — gráficos gerados (.png)
- `csvs/`    — saídas geradas (.csv)
