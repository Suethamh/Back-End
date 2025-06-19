package br.edu.iftm.tspi.porm.sistema_jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Categoria;

@Repository
public interface CategoriaRepository extends 
                            JpaRepository<Categoria, Integer>{

    public List<Categoria> findByNomeContainingIgnoreCase(String nome);

}
