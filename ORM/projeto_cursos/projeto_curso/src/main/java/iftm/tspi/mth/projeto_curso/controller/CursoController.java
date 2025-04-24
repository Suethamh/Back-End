package iftm.tspi.mth.projeto_curso.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iftm.tspi.mth.projeto_curso.domain.Curso;
import iftm.tspi.mth.projeto_curso.dto.CursoDTO;
import iftm.tspi.mth.projeto_curso.dto.ErroDTO;
import jakarta.websocket.server.PathParam;

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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErroDTO.builder()
            .mensagem("Já existe um curso com essa sigla!!")
            .data(LocalDateTime.now())
            .build());
        }
        if(novoCurso.getNome().equals("") || novoCurso.getSigla().equals("")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErroDTO.builder()
                .mensagem("Nome ou sigla não informados!!")
                .data(LocalDateTime.now())
                .build()
            );
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
    public ResponseEntity<?> atualizarCurso(@PathVariable String sigla @RequestBody Curso cursoAtualizado){
        for(CursoDTO curso : cursos){
            if(curso.getSigla().equalsIgnoreCase(sigla)){
                curso.setNome(cursoAtualizado.getNome());
                curso.setSigla(cursoAtualizado.getSigla());
                return ResponseEntity.ok(cursoAtualizado);
            }
        }
    }
}
