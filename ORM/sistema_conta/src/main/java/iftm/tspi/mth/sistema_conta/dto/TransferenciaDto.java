package iftm.tspi.mth.sistema_conta.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaDto {
    
    @NotNull
    private Long origem;

    @NotNull
    private Long destino;

    @NotNull
    @Positive
    private Double valor;
}
