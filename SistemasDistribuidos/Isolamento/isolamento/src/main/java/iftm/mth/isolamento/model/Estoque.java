package iftm.mth.isolamento.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estoque implements Serializable {

    private Integer codigo;
    private Produto produto;
    private Integer quantidadeDisponivel;
    private LocalDateTime ultimaAtualizacao;

}
