package br.edu.iftm.tspi.porm.sistema_jpa.controller;

import java.util.List;
import java.util.Optional;

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

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Categoria;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.CategoriaDto;
import br.edu.iftm.tspi.porm.sistema_jpa.dto.ProdutoDto;
import br.edu.iftm.tspi.porm.sistema_jpa.mapper.CategoriaMapper;
import br.edu.iftm.tspi.porm.sistema_jpa.mapper.ProdutoMapper;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository repository;

    private final CategoriaMapper mapper;

    private final ProdutoMapper produtoMapper;

    public CategoriaController(CategoriaRepository repository,
                               CategoriaMapper mapper,
                               ProdutoMapper produtoMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.produtoMapper = produtoMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listar(@RequestParam(required = false) String nome) {
        List<CategoriaDto> categorias;
        if (nome == null) {
            categorias = mapper.toDtoList(repository.findAll());
        } else {
            categorias = mapper.toDtoList(
                repository.findByNomeContainingIgnoreCase(nome));
        }
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoriaDto> buscarPorId(@PathVariable Integer id) {
        Categoria categoria = repository.findById(id)
                                .orElseThrow(
                                     () -> 
                                     new EntityNotFoundException("Categoria com id "+ id + " não encontrado"));
        return ResponseEntity.ok(mapper.toDto(categoria));
    }

    @GetMapping("{id}/produtos")
    public ResponseEntity<List<ProdutoDto>> buscarProdutosPorCategoriaId(@PathVariable Integer id) {
        Categoria categoria = repository.findById(id)
                                .orElseThrow(
                                     () -> 
                                     new EntityNotFoundException(
                                        "Categoria com id "+ id + " não encontrado"));
        return ResponseEntity.ok(produtoMapper.toDtoList(categoria.getProdutos()));    
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> criar(@Valid @RequestBody CategoriaDto categoriaDto) {
        Categoria categoriaSalva = repository.save(mapper.toEntity(categoriaDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(categoriaSalva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> atualizar(@PathVariable Integer id,
                                        @Valid @RequestBody CategoriaDto categoria) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Categoria com id "+id+" não encontrada");
        }
        categoria.setId(id);
        Categoria categoriaAtualizada = repository.save(mapper.toEntity(categoria));
        return ResponseEntity.ok(mapper.toDto(categoriaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirDoNovoTestamento(@PathVariable Integer id) {
        return repository.findById(id)
                         .map(categoria -> {
                            repository.delete(categoria);
                            return ResponseEntity.noContent().build();
                         })
                         .orElseThrow(
                            () -> new EntityNotFoundException(
                                        "Categoria com id "+id+" não encontrada"));
    }

    @DeleteMapping("/velhoTestamento/{id}")
    public ResponseEntity<?> excluirDoVelhoTestamento(@PathVariable Integer id) {
        Optional<Categoria> categoria = repository.findById(id);
        if (!categoria.isPresent()) {
            throw new EntityNotFoundException("Categoria com id "+id+" não encontrada");
        }
        Categoria categoriaBanco = categoria.get();
        repository.delete(categoriaBanco);
        return ResponseEntity.noContent().build();
    }

}
