package br.edu.iftm.tspi.porm.sistema_jpa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDto {

    private Integer id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

}
