package br.edu.iftm.tspi.porm.sistema_jpa.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="categoriaid")
    private Integer id;

    @Column(name="categoria")
    private String nome;

    @Column(name="descricao")
    private String descricao;

    @OneToMany(mappedBy="categoria")
    private List<Produto> produtos;
}
