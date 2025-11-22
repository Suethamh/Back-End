package iftm.mth.isolamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import iftm.mth.isolamento.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
