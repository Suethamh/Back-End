package iftm.tspi.mth.projeto_curso.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AlunoDTO {
    private String ra;
    private String nome;
    private String email;
    private String cursoSigla;
}
