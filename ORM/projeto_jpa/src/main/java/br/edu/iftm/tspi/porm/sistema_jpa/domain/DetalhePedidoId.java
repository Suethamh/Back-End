package br.edu.iftm.tspi.porm.sistema_jpa.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DetalhePedidoId {

    private Integer pedidoId;

    private Integer produtoId;
}
