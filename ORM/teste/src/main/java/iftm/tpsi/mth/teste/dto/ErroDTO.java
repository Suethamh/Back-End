package iftm.tpsi.mth.teste.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErroDTO {
    
    private String mensagem;
    private Integer idConflito;
    private LocalDateTime data;
}
