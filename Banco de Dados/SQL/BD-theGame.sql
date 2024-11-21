drop database theGame;
create database theGame;
use theGame;

CREATE TABLE jogador (
    cod_jogador INT PRIMARY KEY,
    nome VARCHAR(50),
    nickname VARCHAR(20),
    sexo CHAR(1),
    email VARCHAR(50),
    dt_nasc DATE,
    pontuacao INT,
    moedas INT
);

CREATE TABLE poder (
    cod_poder INT PRIMARY KEY,
    habilidade VARCHAR(50)
);

CREATE TABLE adquire_poder (
    cod_aquisicao INT PRIMARY KEY,
    cod_jogador INT,
    cod_poder INT,
    dt_hora_aquisicao DATETIME,
    FOREIGN KEY(cod_jogador) REFERENCES jogador(cod_jogador),
    FOREIGN KEY(cod_poder) REFERENCES poder(cod_poder)
);

CREATE TABLE cenario (
    cod_cenario INT PRIMARY KEY,
    caracteristicas VARCHAR(30),
    qtde_min_pontos INT
);

CREATE TABLE partida (
    cod_partida INT PRIMARY KEY,
    data_hora_inicio DATETIME,
    data_hora_termino DATETIME,
    pontos_obtidos INT,
    cod_jogador INT,
    cod_cenario INT,
	FOREIGN KEY(cod_jogador) REFERENCES jogador(cod_jogador),
	FOREIGN KEY(cod_cenario) REFERENCES cenario(cod_cenario)
);

INSERT INTO jogador (cod_jogador, nome, nickname, sexo, email, dt_nasc, pontuacao, moedas)
VALUES
(1, 'Roberto Carlos', 'robcar', 'M', 'robcar@gmail.com', '2000-08-07', 3000, 3000),
(2, 'Maria Clara', 'marcla', 'F', 'marcla@gmail.com', '1999-05-07', 5000, 5000),
(3, 'João Marcos', 'jomar', 'M', 'jomar@gmail.com', '1998-01-15', 1000, 5000),
(4, 'Karina Jones', 'kjones', 'F', 'kjones@yahoo.com', '1999-03-05', 2000, 8000);

INSERT INTO poder (cod_poder, habilidade)
VALUES
(1, 'Choque do trovão'),
(2, 'Bola elétrica'),
(3, 'Ataque rápido'),
(4, 'Cauda de ferro'),
(5, 'Teia Elétrica'),
(6, 'Jato de Bolhas'),
(7, 'Redemoinho');

INSERT INTO cenario (cod_cenario, caracteristicas, qtde_min_pontos)
VALUES
(1, 'Terrestre', 1000),
(2, 'Aquatico', 2500),
(3, 'Espacial', 5000);

INSERT INTO partida (cod_partida, data_hora_inicio, data_hora_termino, pontos_obtidos, cod_jogador, cod_cenario)
VALUES
(1, '2023-06-27 19:00:00', '2023-06-27 20:00:00', 300, 1, 1),
(2, '2023-06-27 19:30:00', '2023-06-27 21:00:00', 500, 2, 1),
(3, '2023-06-28 08:00:00', '2023-06-28 12:00:00', 200, 3, 1),
(4, '2023-06-28 08:30:00', '2023-06-28 11:30:00', 200, 1, 2);

INSERT INTO adquire_poder (cod_aquisicao, cod_jogador, cod_poder, dt_hora_aquisicao)
VALUES
(1, 1, 1, '2023-06-20 19:00:00'),
(2, 1, 2, '2023-06-21 10:00:00'),
(3, 2, 1, '2023-06-21 11:00:00'),
(4, 2, 3, '2023-06-23 13:00:00'),
(5, 2, 4, '2023-06-24 15:00:00'),
(6, 3, 5, '2023-06-25 12:00:00'),
(7, 3, 5, '2023-06-26 08:00:00'),
(8, 4, 6, '2023-06-26 12:00:00');

-- 3)Dê o comando select para gerar os seguintes relatórios:
-- a)Listar o nome, nickname e pontuação de todos os jogadores que possuem email gmail.
select j.nome, j.nickname, j.pontuacao
from jogador j
where j.email like ("%gmail%");

-- b)Listar os dados de partidas realizadas no período noturno (após as 18:00).
select p.* from partida p
where hour(p.data_hora_inicio) > 18;

-- c) Listar os dados dos jogadores que tem pontuação e quantidade de moedas acima de 1000.
select j.* from jogador j
where j.pontuacao > 1000 and j.moedas > 1000;

-- d)Sabendo que para jogar no cenário aquático a quantidade mínima de pontos
-- é 2500, liste o nome e nickname dos jogadores que podem jogar nesse cenário.
select j.nome, j.nickname
from jogador j
where j.pontuacao >= 2500;

-- e)Listar os dados de todos os poderes que tenham presente na habilidade a palavra trovão ou elétrica.
select p.* from poder p
where p.habilidade like "%trovão%" or p.habilidade like "%elétrica%";


