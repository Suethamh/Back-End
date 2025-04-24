package iftm.tspi.pbackorm.hello_world.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Contato {
    
    private Integer codigo;
    private String nome;
}
