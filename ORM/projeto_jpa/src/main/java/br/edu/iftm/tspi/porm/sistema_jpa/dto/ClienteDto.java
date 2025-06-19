package br.edu.iftm.tspi.porm.sistema_jpa.dto;

import java.util.List;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Pedido;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDto {

    private String id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String cargo;

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    private String cep;

    @NotBlank(message = "O país é obrigatório")
    private String pais;
    
    private String telefone;

    private String fax;

    private List<Pedido> pedidos;
}
