package iftm.mth.isolamento.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.mth.isolamento.model.ItemPedido;
import iftm.mth.isolamento.model.Pedido;
import iftm.mth.isolamento.service.PedidoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/pedido")
public class PedidoController {
    
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> salvarPedido(@RequestBody Pedido pedido) {
        
        for(ItemPedido item: pedido.getItens()){
            item.setPedido(pedido);
        }

        Pedido pedidoSalvo = pedidoService.salvaPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }
    
}
