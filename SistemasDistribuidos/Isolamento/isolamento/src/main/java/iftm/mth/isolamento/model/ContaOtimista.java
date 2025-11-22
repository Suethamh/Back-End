package iftm.mth.isolamento.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_conta_otimista")
public class ContaOtimista implements Serializable{
    
    @Id
    @Column(name="num_conta")
    private String numero;

    @Column(name="vlr_saldo")
    private Double saldo;

    @Version
    @Column(name="cod_versao")
    private Integer versao;
}
