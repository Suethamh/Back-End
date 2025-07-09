package iftm.tspi.mth.sistema_conta.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tb_conta")
public class Conta {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="cod_conta")
    private long id;

    @Column(name="nome_cliente")
    private String nomeCliente;

    @Column(name="vlr_saldo")
    private Double saldo;

    public void deposita(Double valor) {
        this.saldo += valor;
    }

    public void saque(Double valor){
        this.saldo -= valor;
    }
}
