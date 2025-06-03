package com.iftm.client.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.iftm.client.entities.Client;

@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repositorio;

    @Test
    public void testFindByNameContainingIgnoreCase() {
        List<Client> result = repositorio.findByNameContainingIgnoreCase("maria");
        
        assertEquals(1, result.size());
        assertEquals("Carolina Maria de Jesus", result.get(0).getName());
        assertEquals("10419244771", result.get(0).getCpf());
    }

    @Test
    public void testFindClientsByParamWithBasicData() {
    List<Client> result = repositorio.findClientsByParam("Silva");
    
    assertEquals(0, result.size());
    
    List<Client> allResults = repositorio.findClientsByParam("");
    assertEquals(12, allResults.size());
}

    @Test
    public void testFindClientsWithIncomeMoreThanValor() {
        List<Client> result = repositorio.findClientsWithIncomeMoreThanValor(3000.0);
        
        assertEquals(6, result.size());
        assertEquals("Clarice Lispector", result.get(0).getName());
        assertEquals(3800.0, result.get(0).getIncome());
    }

    @Test
    public void testFindClientsWithIncomeLessThanValor() {
        List<Client> result = repositorio.findClientsWithIncomeLessThanValor(3000.0);
        
        assertEquals(6, result.size());
        assertEquals("Conceição Evaristo", result.get(0).getName());
        assertEquals(1500.0, result.get(0).getIncome());
    }

    @Test
    public void testFindClientsByIncomeRange() {
        Client client3 = new Client(null, "Ana Costa", "11122233344", 1500.0, Instant.parse("1995-08-22T00:00:00Z"), 0);
        Client client4 = new Client(null, "Carlos Oliveira", "55566677788", 5000.0, Instant.parse("1980-03-10T00:00:00Z"), 3);
        repositorio.save(client3);
        repositorio.save(client4);

        List<Client> midRangeClients = repositorio.findClientsWithIncomeMoreThanValor(2000.0);
        midRangeClients.retainAll(repositorio.findClientsWithIncomeLessThanValor(4000.0));
        
        assertEquals(4, midRangeClients.size()'');
    }
}
