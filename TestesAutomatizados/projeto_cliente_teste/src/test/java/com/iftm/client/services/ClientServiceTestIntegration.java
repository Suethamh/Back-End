package com.iftm.client.services;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.iftm.client.dto.ClientDTO;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

@SpringBootTest
public class ClientServiceTestIntegration {

    @Autowired
    private ClientService servico;


    /** Testar o metodo delete da classe service, verificando se o método não retorna nada e exclui o registro quando o id existe.
     * idExistente = 1;
     * resultado esperado = não há retorno de dados ou exceptions.
     */
    @Test
    public void testarApagarClienteQuandoIDExisteNaoRetornandoNada(){
        //assign
        Long idExistente = 1L;
        //act e assert
        assertDoesNotThrow(()->{servico.delete(idExistente);});
    }

    /** Testar o metodo delete da classe service, verificando se o método retorna exception quando o id não existe.
     * idExistente = 100;
     * resultado esperado = ResourceNotFoundException
     */
    @Test
    public void testarApagarClienteQuandoIDNaoExisteRetornaException(){
        //assign
        Long idNaoExistente = 100L;

        //act e assert
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()->{servico.delete(idNaoExistente);});
        assertEquals("Id not found " + idNaoExistente, e.getMessage());
    }

    @Test
    public void testarChamarTodosOsClientesEmUmaPagina(){

        PageRequest requisicao = PageRequest.of(0, 10);


        Page<ClientDTO> resultado = servico.findAllPaged(requisicao);

        Assertions.assertEquals(12, resultado.getTotalElements());
        Assertions.assertEquals(2, resultado.getTotalPages());
    }

    @Test
    public void testarChamarClientePorIdExistente(){
        Long idExistente = 1L;

        Assertions.assertDoesNotThrow(() -> {servico.findById(idExistente);});
    }

    @Test
    public void testarChamarClientePorIdNaoExistenteComExcecao(){
        Long idNaoExistente = 100L;

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {servico.findById(idNaoExistente);});
    }

    @Test
    public void testarFazerUpdateClientePorIdExistente(){
        Long idExistente = 1L;

        ClientDTO clienteDTO = new ClientDTO(idExistente, "Matheus", "12345678910", 3000.00, null, null);
        
        ClientDTO resultado = servico.update(idExistente, clienteDTO);

        Assertions.assertEquals(idExistente, resultado.getId());
        Assertions.assertEquals("Matheus", resultado.getName());
    }

    @Test
    public void testarFazerUpdateClientePorIdNaoExistenteRetornandoExcecao(){
        Long idNaoExistente = 100L;
        Long idExistente = 1L;

        ClientDTO clienteDTO = new ClientDTO(idExistente, "Matheus", "12345678910", 3000.00, null, null);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {servico.update(idNaoExistente, clienteDTO);});
    }

    @Test
    public void inserirUmClientedtoParaClienteNovo(){
        ClientDTO clienteDTOSucesso = new ClientDTO(null, "Matheus", "12345678910", 3000.00, null, null);

        ClientDTO resultado = servico.insert(clienteDTOSucesso);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Matheus", resultado.getName());
    }
}