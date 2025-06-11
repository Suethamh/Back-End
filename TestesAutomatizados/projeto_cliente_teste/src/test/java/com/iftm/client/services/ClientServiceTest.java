package com.iftm.client.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService servico;

    @Mock
    private ClientRepository repositorio;


    /** Testar o metodo delete da classe service, verificando se o método não retorna nada e exclui o registro quando o id existe.
     * idExistente = 1;
     * resultado esperado = não há retorno de dados ou exceptions.
     */
    @Test
    public void testarApagarClienteQuandoIDExisteNaoRetornandoNada(){
        //assign
        Long idExistente = 1L;
        Mockito.doNothing().when(repositorio).deleteById(idExistente);
        //act e assert
        assertDoesNotThrow(()->{servico.delete(idExistente);});
        Mockito.verify(repositorio, Mockito.times(1)).deleteById(idExistente);
    }

    /** Testar o metodo delete da classe service, verificando se o método retorna exception quando o id não existe.
     * idExistente = 100;
     * resultado esperado = ResourceNotFoundException
     */
    @Test
    public void testarApagarClienteQuandoIDNaoExisteRetornaException(){
        //assign
        Long idNaoExistente = 100L;        
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repositorio).deleteById(idNaoExistente);

        //act e assert
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, ()->{servico.delete(idNaoExistente);});
        assertEquals("Id not found " + idNaoExistente, e.getMessage());
        Mockito.verify(repositorio, Mockito.times(1)).deleteById(idNaoExistente);
    }

    @Test
    public void testarChamarTodosOsClientesEmUmaPagina(){

        Client cliente1 = new Client(1L, "Matheus", "12345678910", 1000.00, null, null);
        Client cliente2 = new Client(2L, "Pâmella", "98765432110", 20000.00, null, null);

        List<Client> listaClientes = Arrays.asList(cliente1, cliente2);
        PageRequest requisicao = PageRequest.of(0, 10);

        PageImpl<Client> implementacao = new PageImpl<>(listaClientes, requisicao, listaClientes.size());

        Mockito.when(repositorio.findAll(any(Pageable.class))).thenReturn(implementacao);

        Page<ClientDTO> resultado = servico.findAllPaged(requisicao);

        verify(repositorio, times(1)).findAll(requisicao);

        Assertions.assertEquals(2, resultado.getTotalElements());
        Assertions.assertEquals(1, resultado.getTotalPages());
    }

    @Test
    public void testarChamarClientePorIdExistente(){
        Long idExistente = 1L;

        Mockito.when(repositorio.findById(idExistente)).thenReturn(Optional.of(new Client()));

        Assertions.assertDoesNotThrow(() -> {servico.findById(idExistente);});
    }

    @Test
    public void testarChamarClientePorIdNaoExistenteComExcecao(){
        Long idNaoExistente = 100L;
        Mockito.when(repositorio.findById(idNaoExistente)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {servico.findById(idNaoExistente);});
    }

    @Test
    public void testarFazerUpdateClientePorIdExistente(){
        Long idExistente = 1L;

        Client cliente = new Client(idExistente, "Mateus", "12345678910", 10000.00, null, null);
        ClientDTO clienteDTO = new ClientDTO(idExistente, "Matheus", "12345678910", 3000.00, null, null);

        Mockito.when(repositorio.getOne(idExistente)).thenReturn(cliente);
        Mockito.when(repositorio.save(Mockito.any(Client.class))).thenReturn(cliente);
        
        ClientDTO resultado = servico.update(idExistente, clienteDTO);

        Assertions.assertEquals(idExistente, resultado.getId());
        Assertions.assertEquals("Matheus", resultado.getName());
    }

    @Test
    public void testarFazerUpdateClientePorIdNaoExistenteRetornandoExcecao(){
        Long idNaoExistente = 100L;
        Long idExistente = 1L;

        Client cliente = new Client(idExistente, "Mateus", "12345678910", 10000.00, null, null);
        ClientDTO clienteDTO = new ClientDTO(idExistente, "Matheus", "12345678910", 3000.00, null, null);

        Mockito.when(repositorio.getOne(idExistente)).thenReturn(cliente);
        Mockito.when(repositorio.save(Mockito.any(Client.class))).thenReturn(cliente);
        Mockito.when(repositorio.getOne(idNaoExistente)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {servico.update(idNaoExistente, clienteDTO);});
    }

    @Test
    public void inserirUmClientedtoParaClienteNovo(){
        Long idExistente = 1L;
        ClientDTO clienteDTOSucesso = new ClientDTO(null, "Matheus", "12345678910", 3000.00, null, null);

        Client clienteQueOrepositorioVaiRetornar = new Client(idExistente, "Matheus", "12345678910", 3000.00, null, null);
        Mockito.when(repositorio.save(Mockito.any(Client.class))).thenReturn(clienteQueOrepositorioVaiRetornar);

        ClientDTO resultado = servico.insert(clienteDTOSucesso);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Matheus", resultado.getName());
    }
}