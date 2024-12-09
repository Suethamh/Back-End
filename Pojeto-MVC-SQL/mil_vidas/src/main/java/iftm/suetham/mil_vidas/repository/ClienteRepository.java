package iftm.suetham.mil_vidas.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.stereotype.Component;

import iftm.suetham.mil_vidas.domain.Cliente;

@Component
public class ClienteRepository {
    
    private List<Cliente> clientes;
    
    public ClienteRepository(){
        this.clientes = new ArrayList<>();
        this.clientes.add(new Cliente("pamella.amv@gmail.com", "Pamv18++", "Pâmella Araújo", "Bilu"));
        this.clientes.add(new Cliente("matheushso06@gmail.com", "Matheus@2504", "Matheus Oliveira", "Suetham"));
    }

    public List<Cliente> getClientes(){
        return this.clientes;
    }


    public List<Cliente> buscaClientePorNome(String nome) {
        List<Cliente> clienteBusca = new ArrayList<>();
        for(Cliente cliente : this.clientes){
            if(cliente.getNome().toLowerCase().contains(nome.toLowerCase())){
                clienteBusca.add(cliente);
            }
        }
        return clienteBusca;
    }

    public Cliente buscaClientePorLogin(String login) {
        Cliente clienteBusca = new Cliente(login);
        int index = clientes.indexOf(clienteBusca);
        if(index != -1){
            return clientes.get(index);
        }else{
            return null;
        }
    }

    public void novoCliente(Cliente cliente){
        clientes.add(cliente);
    }

    public boolean delete(String login){
        Cliente cliente = new Cliente(login);
        return clientes.remove(cliente);
    }

    public boolean update(Cliente cliente){
        int index = clientes.indexOf(cliente);
        if(index != -1){
            clientes.set(index, cliente);
            return true;
        }
        return false;
    }
}
