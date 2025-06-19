package br.edu.iftm.tspi.porm.sistema_jpa.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name="pedidos")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="PedidoID", nullable=false)
    private Integer id;

    @Column(name="datapedido")
    private LocalDateTime dataPedido;

    @ManyToOne
    @JoinColumn(name="ClienteID",nullable=false)
    private Cliente cliente;

    @OneToMany(mappedBy="pedido",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    private List<DetalhePedido> detalhesPedido;


}
