package iftm.tpsi.mth.teste.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.client.RestClient.ResponseSpec;

import iftm.tpsi.mth.teste.domain.Contato;
import iftm.tpsi.mth.teste.dto.ErroDTO;

@RestController
@RequestMapping("/contatos")
public class TesteController {

    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarPorId(@PathVariable Integer id){
        List<Contato> contatos = Arrays.asList(
            new Contato(1, "Matheus"),
            new Contato(2, "Pâmella")
        );
        for(Contato contato : contatos){
            if(contato.getCodigo().equals(id)){
                return ResponseEntity.ok(contato);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Contato>> buscar(@RequestParam(required = false) String nome){
        List<Contato> contatos = Arrays.asList(
            new Contato(1, "Matheus"),
            new Contato(2, "Pâmella")
        );

        if(nome == null){
            return ResponseEntity.ok(contatos);
        }
        List<Contato> resposta = new ArrayList<>();
        for(Contato contato : contatos){
            if(contato.getNome().toLowerCase().contains(nome)){
                resposta.add(contato);
            }
        }
        return ResponseEntity.ok(resposta);
    
    }

    private List<Contato> contatos = new ArrayList<>();

    @PostMapping
    public ResponseEntity<?> novo(@RequestBody Contato novoContato){
        boolean existe = contatos.stream().anyMatch(contato -> contato.getCodigo().equals(novoContato.getCodigo()));

        if(existe) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErroDTO("Já existe contato de código ", novoContato.getCodigo(), LocalDateTime.now())
            );
        }
        if(novoContato.getNome() == null || novoContato.getNome().equals("")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErroDTO("Nome não informado", null, LocalDateTime.now())
            );
        }
        contatos.add(novoContato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody Contato contatoAtualizado) {
        for(Contato contato : contatos){
            if(contato.getCodigo().equals(id)){
                contato.setNome(contatoAtualizado.getNome());
                return ResponseEntity.ok(contatoAtualizado);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErroDTO("Contato não encontrado", id, LocalDateTime.now())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id){
        boolean removido = contatos.removeIf(contato -> contato.getCodigo().equals(id));

        if(removido){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErroDTO("Contato não encontrado", id, LocalDateTime.now())
        );

    }

}
