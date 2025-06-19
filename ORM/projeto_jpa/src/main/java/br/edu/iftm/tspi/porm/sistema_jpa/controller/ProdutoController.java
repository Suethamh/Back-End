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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Produto;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.ProdutoDto;
import br.edu.iftm.tspi.porm.sistema_jpa.mapper.ProdutoMapper;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private ProdutoRepository repository;

    private ProdutoMapper mapper;

    public ProdutoController(ProdutoRepository repository,
                             ProdutoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> listarTodos(
                    @RequestParam(required = false) String nome) {
        List<Produto> produtos;
        if (nome == null) {
            produtos = repository.findAll();
        } else {
            produtos = repository.findByNomeContainingIgnoreCase(nome);
        }

        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();  
        }
        return ResponseEntity.ok(mapper.toDtoList(produtos)); 
    }

    @GetMapping("{id}")
    public ResponseEntity<ProdutoDto> listPorId(@PathVariable Integer id) {
         Produto produto = repository.findById(id)
                                     .orElseThrow(() -> 
                                         new EntityNotFoundException(
                                        "Produto não encontrado para o ID: "+id));
        
        return ResponseEntity.ok(mapper.toDto(produto));
    }

    @PostMapping
    public ResponseEntity<ProdutoDto> criar(@Valid @RequestBody ProdutoDto produtoDto) {
        Produto produtoSalvo = repository.save(mapper.toEntity(produtoDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(mapper.toDto(produtoSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDto> atualizar(@PathVariable Integer id,
                            @Valid @RequestBody ProdutoDto dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Produto com ID "+id+" não encontrado");
        }
        dto.setCodigo(id);
        Produto produtoAtualizado = repository.save(mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDto(produtoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        return repository.findById(id)
                    .map(produto -> {
                        repository.delete(produto);
                        return ResponseEntity.noContent().build();
                    })
                    .orElseThrow(() -> new EntityNotFoundException("Produto com id "+id+" não encontrado"));
    }

}
