package br.edu.iftm.tspi.porm.sistema_jpa.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ErroDto {

    private String message;

    private LocalDateTime dataHora;

}
