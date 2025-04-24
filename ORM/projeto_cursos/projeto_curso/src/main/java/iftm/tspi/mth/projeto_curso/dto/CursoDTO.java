package iftm.tspi.mth.projeto_curso.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CursoDTO {
    private String sigla;
    private String nome;
    private String descricao;
}
