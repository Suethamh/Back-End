package br.edu.iftm.tspi.porm.sistema_jpa.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.edu.iftm.tspi.porm.sistema_jpa.annotation.ClienteExists;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Cliente;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {

    private Integer id;

    private LocalDateTime dataPedido;

    @ClienteExists(message = "Não existe ess Cliente")
    private Cliente cliente;

    private List<DetalhePedido> detalhesPedido;
}
