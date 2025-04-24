package iftm.tspi.mth.projeto_curso.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Aluno {
    private String ra;
    private String nome;
    private String email;
    private Curso curso;
}
