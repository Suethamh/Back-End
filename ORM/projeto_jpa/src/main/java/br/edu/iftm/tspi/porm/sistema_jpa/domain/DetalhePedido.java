package br.edu.iftm.tspi.porm.sistema_jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="detalhes_pedido")
public class DetalhePedido {

    @EmbeddedId
    private DetalhePedidoId id;

    @Column(name="precoVenda")
    private Double precoVenda;

    private Short quantidade;

    private Double desconto;

    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name="produtoID",nullable=false)
    private Produto produto;

    @ManyToOne
    @MapsId("pedidoId")
    @JoinColumn(name="pedidoID",nullable=false)
    private Pedido pedido;

}
