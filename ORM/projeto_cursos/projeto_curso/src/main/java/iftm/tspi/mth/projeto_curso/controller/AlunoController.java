package iftm.tspi.mth.projeto_curso.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iftm.tspi.mth.projeto_curso.domain.Aluno;
import iftm.tspi.mth.projeto_curso.dto.AlunoDTO;
import iftm.tspi.mth.projeto_curso.exception.ConflitoDeDadosException;
import iftm.tspi.mth.projeto_curso.exception.RecursoNaoEncontradoException;
import iftm.tspi.mth.projeto_curso.exception.RequesicaoInvalidaException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private List<AlunoDTO> alunos = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarAlunos(){
        return ResponseEntity.ok(alunos);
    }

    @PostMapping
    public ResponseEntity<?> adicionarAluno(@RequestBody AlunoDTO novoAluno){
        boolean existe = alunos.stream().anyMatch(aluno -> aluno.getRa().equals(novoAluno.getRa()));

        if(existe){
            throw new ConflitoDeDadosException("Já existe aluno com esse RA");
        }
        if(novoAluno.getNome().equals("") || novoAluno.getRa().equals("")){
            throw new RequesicaoInvalidaException("Nome ou RA do aluno não informados");
        }
        alunos.add(novoAluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
    }

    @GetMapping("/{ra}")
    public ResponseEntity<?> buscarPorRa(@PathVariable String ra){
         if(ra == null){
            return ResponseEntity.ok(alunos);
        }
        List<AlunoDTO> resposta = new ArrayList<>();
        for (AlunoDTO aluno : alunos){
            if(aluno.getRa().equalsIgnoreCase(ra)){
                resposta.add(aluno);
            }
        }
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{ra}")
    public ResponseEntity<?> atualizarAluno(@PathVariable String ra, @RequestBody Aluno alunoAtualizado){
        for(AlunoDTO aluno : alunos){
            if(aluno.getRa().equalsIgnoreCase(ra)){
                aluno.setNome(alunoAtualizado.getNome());
                return ResponseEntity.ok(alunoAtualizado);
            }
        }

        throw new RecursoNaoEncontradoException("Aluno não encontrado");
    }

    @DeleteMapping("/{ra}")
    public ResponseEntity<?> excluirCurso(@PathVariable String ra){
        boolean removido = alunos.removeIf(aluno -> aluno.getRa().equals(ra));
        if(removido){
            return ResponseEntity.noContent().build();
        }
        throw new RecursoNaoEncontradoException("Aluno não encontrado");
    }

}
