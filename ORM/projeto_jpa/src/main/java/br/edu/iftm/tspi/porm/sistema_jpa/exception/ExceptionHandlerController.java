package br.edu.iftm.tspi.porm.sistema_jpa.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.iftm.tspi.porm.sistema_jpa.dto.ErroDto;
import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroDto> tratarNaoEncontrado(EntityNotFoundException e) {
        ErroDto erro = ErroDto.builder()
                                .message(e.getMessage())
                                .dataHora(LocalDateTime.now())
                                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDto> tratarBadRequest(MethodArgumentNotValidException ex) {

        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
            erro -> erros.put(erro.getField(), erro.getDefaultMessage()) 
        );

        List<FieldError> lista = ex.getBindingResult().getFieldErrors();
        for (FieldError erro: lista) {
            erros.put(erro.getField(), erro.getDefaultMessage());
        }

        ErroDto erro = ErroDto.builder()
                              .message(erros.toString())
                              .dataHora(LocalDateTime.now())
                              .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    } 

}
