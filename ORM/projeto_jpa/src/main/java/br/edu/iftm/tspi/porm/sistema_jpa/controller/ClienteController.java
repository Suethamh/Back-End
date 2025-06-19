package br.edu.iftm.tspi.porm.sistema_jpa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Cliente;
import br.edu.iftm.tspi.porm.sistema_jpa.domain.Pedido;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.ClienteDto;
import br.edu.iftm.tspi.porm.sistema_jpa.mapper.ClienteMapper;
import br.edu.iftm.tspi.porm.sistema_jpa.mapper.PedidoMapper;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    private final ClienteRepository repository;

    private final ClienteMapper mapper;

    private final PedidoMapper pedidoMapper;

    public ClienteController(ClienteRepository repository, ClienteMapper mapper, PedidoMapper pedidoMapper){
        this.repository = repository;
        this.mapper = mapper;
        this.pedidoMapper = pedidoMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarClientes(@RequestParam(required = false) String nome){
        List<Cliente> clientes;

        if(nome == null){
            clientes = repository.findAll();
        }else {
            clientes = repository.findByNameContainingIgnoreCase(nome);
        }

        return ResponseEntity.ok(mapper.toDtoList(clientes));

    }

    @GetMapping("{clienteId}")
    public ResponseEntity<ClienteDto> listarClientePorId(@PathVariable String id){
        Cliente cliente = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cliente com id: " + id + " não encontrado")
        );

        return ResponseEntity.ok(mapper.toDto(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteDto> criarCliente(@Valid @RequestBody ClienteDto clienteDto){
        Cliente clienteSalvo = repository.save(mapper.toEntity(clienteDto));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(clienteSalvo));
    }

    @PutMapping("{clienteId}")
    public ResponseEntity<ClienteDto> atualizarCliente(@PathVariable String id, 
    @Valid @RequestBody ClienteDto clienteDto){

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Cliente com id: "+ id +" não encontrado");
        }

        clienteDto.setId(id);
        Cliente cleinteAtualizado = repository.save(mapper.toEntity(clienteDto));

        return ResponseEntity.ok(mapper.toDto(cleinteAtualizado));                                             
    }

    @DeleteMapping("{clienteID}")
    public ResponseEntity<?> deletarCliente(@PathVariable String id){
        return repository.findById(id).map(cliente -> {
            repository.delete(cliente);
            return ResponseEntity.notFound().build();
        }).orElseThrow(() -> new EntityNotFoundException("Cliente com id: "+id+" não encontrado"));
    }
    
    @GetMapping("{clienteId}/pedidos")
    public ResponseEntity<?> listarPedidosPorCliente(@PathVariable String id){
        Cliente cliente = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente com id: "+id+" não encontrado"));
        List<Pedido> pedidos;

        pedidos = cliente.getPedidos();

        return ResponseEntity.ok(pedidoMapper.toDtoList(pedidos));
    }
}
