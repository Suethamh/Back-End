package br.edu.iftm.tspi.porm.sistema_jpa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedido;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.DetalhePedidoId;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Pedido;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.DetalhePedidoDto;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.PedidoDto;
import br.edu.iftm.tspi.porm.sistema_jpa.mapper.DetalhePedidoMapper;
import br.edu.iftm.tspi.porm.sistema_jpa.mapper.PedidoMapper;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.DetalhePedidoRepository;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repository;
    private final PedidoMapper mapper;
    private final DetalhePedidoRepository detalhePedidoRepository;
    private final DetalhePedidoMapper detalhePedidoMapper;


    public PedidoController(PedidoRepository repository, PedidoMapper mapper, DetalhePedidoRepository detalhePedidoRepository, DetalhePedidoMapper detalhePedidoMapper){
        this.repository = repository;
        this.mapper = mapper;
        this.detalhePedidoMapper = detalhePedidoMapper;
        this.detalhePedidoRepository = detalhePedidoRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<PedidoDto>> listarPedidos(){
        List<Pedido> pedidos = repository.findAll();

        return ResponseEntity.ok(mapper.toDtoList(pedidos));
    }

    @GetMapping("{id}")
    public ResponseEntity<PedidoDto> listarPedidoPorId(@PathVariable Integer id){
        Pedido pedido = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido com id: "+id+" não encontrado"));
        
        return ResponseEntity.ok(mapper.toDto(pedido));
    }

    @PostMapping
    public ResponseEntity<PedidoDto> criarPedido(@Valid @RequestBody PedidoDto pedidoDto){
        Pedido pedidoSalvo = repository.save(mapper.toEntity(pedidoDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(pedidoSalvo));
    }

    @PutMapping("{id}")
    public ResponseEntity<PedidoDto> atualizarPedido(@PathVariable Integer id, @Valid @RequestBody PedidoDto pedidoDto){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Pedido com id: "+id+" não encontrado");
        }

        pedidoDto.setId(id);
        Pedido pedidoAtualizado = repository.save(mapper.toEntity(pedidoDto));

        return ResponseEntity.ok(mapper.toDto(pedidoAtualizado));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletarPedido(@PathVariable Integer id){
        return repository.findById(id).map(pedido -> {
            repository.delete(pedido);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new EntityNotFoundException("Pedido com id: "+id+" não encontrado"));
    }

    @GetMapping("{id}/itens")
    public ResponseEntity<List<DetalhePedidoDto>> listarItensPorPedido(@PathVariable Integer id){
        List<DetalhePedido> detalhePedidos = detalhePedidoRepository.findByPedidoId(id);

        if(detalhePedidos.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(detalhePedidoMapper.toDtoList(detalhePedidos));
    }

    @PostMapping("{id}/itens")
    public ResponseEntity<DetalhePedidoDto> adicionarItemNoPedido(@PathVariable Integer id, @Valid @RequestBody DetalhePedidoDto detalhePedidoDto){
        Pedido pedido = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido com id: "+id+" não encontrado"));
        detalhePedidoDto.setPedido(pedido);
        DetalhePedido detalhePedidoSalvo = detalhePedidoRepository.save(detalhePedidoMapper.toEntity(detalhePedidoDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhePedidoMapper.toDto(detalhePedidoSalvo));
    }

    @PutMapping("{id}/itens/{produtoId}")
    public ResponseEntity<DetalhePedidoDto> atualizarItemPorPedido(@PathVariable Integer id, @PathVariable Integer produtoId, @Valid @RequestBody DetalhePedidoDto detalhePedidoDto){
        Pedido pedido = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pedido com id: "+id+" não encontrado"));
        
        DetalhePedidoId detalhePedidoCompositeId = new DetalhePedidoId(id, produtoId);

        DetalhePedido detalhePedidoExistente = detalhePedidoRepository.findById(detalhePedidoCompositeId)
            .orElseThrow(() -> new EntityNotFoundException("Item com produtoId: " + produtoId + " não encontrado no pedido com id: " + id));

        if (detalhePedidoDto.getProduto() != null && detalhePedidoDto.getProduto().getCodigo() != null &&
        !detalhePedidoDto.getProduto().getCodigo().equals(produtoId)) {
        throw new IllegalArgumentException("O ID do produto no corpo da requisição (" +
                                            detalhePedidoDto.getProduto().getCodigo() +
                                            ") não corresponde ao ID do produto na URL (" + produtoId + ").");
        }

        detalhePedidoDto.setPedido(pedido);
        detalhePedidoDto.setId(detalhePedidoCompositeId);
        
        DetalhePedido detalhePedidoAtualizado = detalhePedidoRepository.save(detalhePedidoMapper.toEntity(detalhePedidoDto));

        return ResponseEntity.ok(detalhePedidoMapper.toDto(detalhePedidoAtualizado));
    }

    @DeleteMapping("{id}/itens/{produtoId}")
    public ResponseEntity<?> deletarItemDoPedido(@PathVariable("id") Integer pedidoId, @PathVariable Integer produtoId) {
        if (!repository.existsById(pedidoId)) {
            throw new EntityNotFoundException("Pedido com id: " + pedidoId + " não encontrado");
        }

        DetalhePedidoId detalhePedidoId = new DetalhePedidoId(pedidoId, produtoId);
        return detalhePedidoRepository.findById(detalhePedidoId)
                .map(item -> {
                    detalhePedidoRepository.deleteById(detalhePedidoId);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item com produtoId: " + produtoId + " não encontrado no pedido com id: " + pedidoId));
    }

}
