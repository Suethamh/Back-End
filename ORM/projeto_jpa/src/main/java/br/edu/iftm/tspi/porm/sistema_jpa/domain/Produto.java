package br.edu.iftm.tspi.porm.sistema_jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="produtos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ProdutoID")
    private Integer codigo;  

    @Column(name="produtonome",nullable=false)
    private String nome;

    @Column(name="preco")
    private Double preco;

    @Column(name="unidadesemestoque")
    private Short estoque;

    @Column(name="Imagem")
    private String caminhoImagem;

    @ManyToOne
    @JoinColumn(name="categoriaid",nullable=false)
    private Categoria categoria;

} 