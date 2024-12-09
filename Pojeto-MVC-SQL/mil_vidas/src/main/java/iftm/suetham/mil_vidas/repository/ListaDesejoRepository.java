package iftm.suetham.mil_vidas.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import iftm.suetham.mil_vidas.domain.Cliente;
import iftm.suetham.mil_vidas.domain.Livro;
import iftm.suetham.mil_vidas.domain.ListaDesejo;

@Component
public class ListaDesejoRepository {

    private List<ListaDesejo> listaDesejos;

    public ListaDesejoRepository() {
        this.listaDesejos = new ArrayList<>();
        // Dados iniciais para teste
        this.listaDesejos.add(new ListaDesejo(1, new Livro(1, "O Corredor tem medo do perigo", "Carl Deuker", "RealIdade", "Suspense", 174, 4.0), new Cliente("user1", "senha1", "João", "joao123"), "2024-12-01"));
        this.listaDesejos.add(new ListaDesejo(2, new Livro(2, "Eleanor & Park", "Rainbow Rowell", "Novo Século", "Romance", 287, 5.0), new Cliente("user2", "senha2", "Maria", "maria123"), "2024-12-02"));
    }

    public List<ListaDesejo> getListaDesejos() {
        return this.listaDesejos;
    }

    public List<ListaDesejo> buscaListaDesejosPorCliente(String clienteNome) {
        List<ListaDesejo> listaBusca = new ArrayList<>();
        for (ListaDesejo ld : this.listaDesejos) {
            if (ld.getCliente().getLogin().equalsIgnoreCase(clienteNome)) {
                listaBusca.add(ld);
            }
        }
        return listaBusca;
    }

    public ListaDesejo buscaListaDesejoPorCodigo(int cod_wl) {
        ListaDesejo listaBusca = new ListaDesejo();
        listaBusca.setCod_wl(cod_wl);
        int index = listaDesejos.indexOf(listaBusca);
        if (index != -1) {
            return listaDesejos.get(index);
        }
        return null;
    }

    public void novaListaDesejo(ListaDesejo listaDesejo) {
        listaDesejos.add(listaDesejo);
    }

    public boolean delete(int cod_wl) {
        ListaDesejo listaDesejo = new ListaDesejo();
        listaDesejo.setCod_wl(cod_wl);
        return listaDesejos.remove(listaDesejo);
    }

    public boolean update(ListaDesejo listaDesejo) {
        int index = listaDesejos.indexOf(listaDesejo);
        if (index != -1) {
            listaDesejos.set(index, listaDesejo);
            return true;
        }
        return false;
    }
}
