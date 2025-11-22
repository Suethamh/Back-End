package iftm.mth.isolamento.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_pedido")
public class Pedido implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="num_pedido")
    private Integer numero;

    @Column(name="data_pedido")
    private LocalDate data;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemPedido> itens;

}
