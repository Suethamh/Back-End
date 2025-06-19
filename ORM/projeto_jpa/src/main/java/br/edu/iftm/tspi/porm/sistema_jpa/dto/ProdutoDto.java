package br.edu.iftm.tspi.porm.sistema_jpa.dto;

import br.edu.iftm.tspi.porm.sistema_jpa.annotation.CategoriaExists;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoDto {

    private Integer codigo;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Positive(message = "Preço deve ser maior do que 0")
    private Double preco;

    @Min(value = 0, message = "Não exsite estoque negativo")
    private Integer estoque;

    private String caminhoImagem;

    @CategoriaExists(message=" Não existe categoria com o ID informado")
    private Integer categoriaId;


}
