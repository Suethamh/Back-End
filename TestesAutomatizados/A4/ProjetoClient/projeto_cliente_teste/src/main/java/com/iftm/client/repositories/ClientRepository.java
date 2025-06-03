package com.iftm.client.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public List<Client> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:nome)")
    public Client findByName(String nome);

    @Query("SELECT c from Client c where c.name like %:param%")
    public List<Client> findClientsByParam(String param);

    @Query("SELECT c FROM Client c WHERE c.income < :valor")
    List<Client> findClientsWithIncomeLessThanValor(Double valor);
    
    @Query("SELECT c FROM Client c WHERE c.income > :valor")
    List<Client> findClientsWithIncomeMoreThanValor(Double valor);

}