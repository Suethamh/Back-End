package br.edu.iftm.tspi.porm.sistema_jpa.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="clientes")
public class Cliente implements Serializable{

    @Id
    @Column(name="ClienteID",nullable=false,columnDefinition="CHAR(5)")
    private String id;

    private String nome;

    private String cargo;

    private String endereco;

    private String cidade;

    private String cep;

    private String pais;

    private String telefone;

    @Column(name="Fax")
    private String fax;

    @OneToMany(mappedBy="cliente")
    private List<Pedido> pedidos;


}
