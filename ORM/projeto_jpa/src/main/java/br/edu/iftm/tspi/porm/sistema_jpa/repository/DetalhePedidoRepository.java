package br.edu.iftm.tspi.porm.sistema_jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedido;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedidoId;

@Repository
public interface DetalhePedidoRepository extends JpaRepository<DetalhePedido, DetalhePedidoId>{

    List<DetalhePedido> findByPedidoId(Integer id);

}
