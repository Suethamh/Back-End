package iftm.mth.isolamento.service;

import org.springframework.stereotype.Service;

import iftm.mth.isolamento.model.ItemPedido;
import iftm.mth.isolamento.model.Pedido;
import iftm.mth.isolamento.repository.EstoqueRepository;
import iftm.mth.isolamento.repository.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final EstoqueRepository estoqueRepository;

    public PedidoService(PedidoRepository pedidoRepository, EstoqueRepository estoqueRepository){
        this.pedidoRepository = pedidoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public Pedido salvaPedido(Pedido pedido){
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        
        if(!pedido.getItens().isEmpty()){
            throw new RuntimeException("Não identificado");
        }

        for(ItemPedido item: pedido.getItens()){
            estoqueRepository.atualizaEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        return pedidoSalvo;
    }
    
}