package iftm.tspi.mth.matematica.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import iftm.tspi.mth.matematica.dto.ErroDTO;

@RestControllerAdvice
public class ExceptionHandlerController {
    
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> tratarNaoEncontrado(RecursoNaoEncontradoException ex){
        ErroDTO erro = ErroDTO.builder()
                        .mensagem(ex.getMessage())
                        .data(LocalDateTime.now())
                        .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(RequesicaoInvalidaException.class)
    public ResponseEntity<ErroDTO> requesicaoInvalida(RequesicaoInvalidaException ex){
        ErroDTO erro = ErroDTO.builder()
                        .mensagem(ex.getMessage())
                        .data(LocalDateTime.now())
                        .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ConflitoDeDadosException.class)
    public ResponseEntity<ErroDTO> conflitoDeDados(ConflitoDeDadosException ex){
        ErroDTO erro = ErroDTO.builder()
                        .mensagem(ex.getMessage())
                        .data(LocalDateTime.now())
                        .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

}
