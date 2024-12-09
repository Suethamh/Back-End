package iftm.suetham.mil_vidas.domain;

public class ListaDesejo {
    private int cod_wl;
    private Livro livro;
    private Cliente cliente;
    private String data_inclusao;

    public ListaDesejo() {
    }
    
    public ListaDesejo(int cod_wl) {
        this.cod_wl = cod_wl;
    }

    public ListaDesejo(int cod_wl, Livro livro, Cliente cliente, String data_inclusao) {
        this.cod_wl = cod_wl;
        this.livro = livro;
        this.cliente = cliente;
        this.data_inclusao = data_inclusao;
    }

    public int getCod_wl() {
        return cod_wl;
    }

    public void setCod_wl(int cod_wl) {
        this.cod_wl = cod_wl;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getData_inclusao() {
        return data_inclusao;
    }

    public void setData_inclusao(String data_inclusao) {
        this.data_inclusao = data_inclusao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cod_wl;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ListaDesejo other = (ListaDesejo) obj;
        if (cod_wl != other.cod_wl)
            return false;
        return true;
    }
    
}
