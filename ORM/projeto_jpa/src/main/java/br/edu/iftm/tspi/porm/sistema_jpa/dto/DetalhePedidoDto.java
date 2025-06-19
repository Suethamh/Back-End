package br.edu.iftm.tspi.porm.sistema_jpa.dto;

import br.edu.iftm.tspi.porm.sistema_jpa.annotation.PedidoExists;
import br.edu.iftm.tspi.porm.sistema_jpa.annotation.ProdutoExists;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedidoId;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Pedido;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Produto;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalhePedidoDto {
    
    private DetalhePedidoId id;

    @Min(value = 1, message = "Não existe pedido com valor menor que 1")
    private Double precoVenda;

    @Min(value = 1, message = "Não existe quantidade 0")
    private Short quantidade;

    @Min(value = 0, message = "Não existe desconto negativo")
    private Double desconto;

    @ProdutoExists(message = "Não existe produto com esse ID")
    private Produto produto;

    @PedidoExists(message = "Não existe pedido com esse ID")
    private Pedido pedido;
}
