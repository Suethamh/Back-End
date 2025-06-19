package br.edu.iftm.tspi.porm.sistema_jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iftm.tspi.porm.sistema_jpa.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    public List<Cliente> findByNameContainingIgnoreCase(String nome);
}