package iftm.tspi.pbackorm.hello_world.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import iftm.tspi.pbackorm.hello_world.domain.Contato;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/contatos")
public class ContatoController {
    @GetMapping
    public ResponseEntity<List<Contato>> getContatos(){
        List<Contato> contatos = Arrays.asList(new Contato(1, "Goku"),
                                               new Contato(2, "Vegeta"));

        return ResponseEntity.ok().body(contatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarPorId(@PathVariable Integer id) {
        List<Contato> contatos = Arrays.asList(new Contato(1, "Goku"),
                                               new Contato(2, "Vegeta"));

        for(Contato contato : contatos) {
            if(contato.getCodigo().equals(id)) {
                return ResponseEntity.ok().body(contato);
            }
        }

        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Contato>> buscarPorNome(@RequestParam(required = true) String nome) {
        List<Contato> contatos = Arrays.asList(new Contato(1, "Goku"),
                                               new Contato(2, "Vegeta"));
        List<Contato> contatosEncontrados = new ArrayList<>();

        for(Contato contato : contatos) {
            if (contato.getNome().toLowerCase().equals(nome.toLowerCase())) {
                contatosEncontrados.add(contato);
            }

        }
        return ResponseEntity.ok().body(contatosEncontrados);
    }
    
}
