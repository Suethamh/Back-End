package iftm.tspi.mth.projeto_curso.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErroDTO {
    private String mensagem;
    private LocalDateTime data;
}
