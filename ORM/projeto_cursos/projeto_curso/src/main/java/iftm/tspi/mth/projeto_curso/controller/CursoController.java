package iftm.tspi.mth.projeto_curso.controller;

import java.util.ArrayList;
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

import iftm.tspi.mth.projeto_curso.domain.Curso;
import iftm.tspi.mth.projeto_curso.dto.AlunoDTO;
import iftm.tspi.mth.projeto_curso.dto.CursoDTO;
import iftm.tspi.mth.projeto_curso.exception.ConflitoDeDadosException;
import iftm.tspi.mth.projeto_curso.exception.RecursoNaoEncontradoException;
import iftm.tspi.mth.projeto_curso.exception.RequesicaoInvalidaException;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    
    private List<CursoDTO> cursos = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCurso(){
        return ResponseEntity.ok().body(cursos);
    }

    @PostMapping
    public ResponseEntity<?> novoCurso(@RequestBody CursoDTO novoCurso){
        boolean existe = cursos.stream().anyMatch(curso -> curso.getSigla().equals(novoCurso.getSigla()));
        
        if(existe) {
            throw new ConflitoDeDadosException("Já existe um curso com essa sigla");
        }
        if(novoCurso.getNome().equals("") || novoCurso.getSigla().equals("")){
            throw new RequesicaoInvalidaException("Nome ou sigla não informada");
        }

        cursos.add(novoCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCurso);
    }

    @GetMapping("/{sigla}")
    public ResponseEntity<List<CursoDTO>> buscarPorSigla(@PathVariable String sigla){
        if(sigla == null){
            return ResponseEntity.ok(cursos);
        }
        List<CursoDTO> resposta = new ArrayList<>();
        for (CursoDTO curso : cursos){
            if(curso.getSigla().equalsIgnoreCase(sigla)){
                resposta.add(curso);
            }
        }
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{sigla}")
    public ResponseEntity<?> atualizarCurso(@PathVariable String sigla, @RequestBody Curso cursoAtualizado){
        for(CursoDTO curso : cursos){
            if(curso.getSigla().equalsIgnoreCase(sigla)){
                curso.setNome(cursoAtualizado.getNome());
                curso.setSigla(cursoAtualizado.getSigla());
                return ResponseEntity.ok(cursoAtualizado);
            }
        }

        throw new RecursoNaoEncontradoException("Curso não encontrado");
    }

    @DeleteMapping("/{sigla}")
    public ResponseEntity<?> excluirCurso(@PathVariable String sigla){
        boolean vazio = cursos.isEmpty();
        

        if(!vazio){
            boolean removido = cursos.removeIf(curso -> curso.getSigla().equals(sigla));
            if(removido){
                return ResponseEntity.noContent().build();
            }
        }else{
            throw new ConflitoDeDadosException("Existem alunos nesse curso. Não é possivel excluir.");
        }

        throw new RecursoNaoEncontradoException("Curso não encontrado");
    }

    @GetMapping("/{sigla}/alunos")
    public ResponseEntity<?> alunosPorCurso(@PathVariable String sigla) {
        List<AlunoDTO> alunos = new ArrayList<>();
        return ResponseEntity.ok(alunos);
    }
    
}
