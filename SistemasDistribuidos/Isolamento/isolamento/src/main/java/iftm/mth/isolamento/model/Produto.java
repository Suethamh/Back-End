package iftm.mth.isolamento.model;

import java.io.Serializable;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_produto")
public class Produto implements Serializable{
    
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "cod_produto")
    private Integer id;

    @Column(name = "nome_produto")   
    private String nome;
    
    @Column(name = "vlr_produto")
    private Double valor;

    @Column(name = "qtd_estoque")
    private Integer estoque;
}
