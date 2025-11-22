package iftm.mth.isolamento.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_conta_pessimista")
public class ContaPessimista implements Serializable {
    
    @Id
    @Column(name="num_conta")
    private String numero;

    @Column(name="vlr_saldo")
    private Double saldo;
}
