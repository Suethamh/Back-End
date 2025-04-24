package iftm.tspi.mth.projeto_curso.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Curso {
    private String sigla;
    private String nome;
    private String descricao;
}
