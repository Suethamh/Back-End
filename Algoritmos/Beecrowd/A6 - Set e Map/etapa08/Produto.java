package etapa08;

import java.util.Objects;

public class Produto {
    
    public String id;
    public String nome;
    
    public Produto(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() == o.getClass()) return false;
        return id.equals(((Produto) o).getId()); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString(){
        return "Produto [id=" + id +", nome=" + nome + "]";
    }
}
