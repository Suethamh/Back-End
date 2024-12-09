package iftm.suetham.mil_vidas.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import iftm.suetham.mil_vidas.domain.Livro;

@Component
public class LivroRepository {
    
    private List<Livro> livros;
    
    public LivroRepository(){
        this.livros = new ArrayList<>();
        this.livros.add(new Livro(1, "O Corredor tem medo do perigo", "Carl Deuker", "RealIdade", "Suspense", 174, 4.0));
        this.livros.add(new Livro(2, "Eleanor & Park", "rainbow rowell", "novo século", "romance", 287, 5.0));
    }

    public List<Livro> getLivros(){
        return this.livros;
    }


    public List<Livro> buscaLivroPorTitulo(String titulo) {
        List<Livro> livroBusca = new ArrayList<>();
        for(Livro livro : this.livros){
            if(livro.getTitulo().toLowerCase().contains(titulo.toLowerCase())){
                livroBusca.add(livro);
            }
        }
        return livroBusca;
    }

    public Livro buscaLivroPorCodigo(int codigo) {
        Livro livroBusca = new Livro(codigo);
        int index = livros.indexOf(livroBusca);
        if(index != -1){
            return livros.get(index);
        }else{
            return null;
        }
    }

    public void novoLivro(Livro livro){
        livros.add(livro);
    }

    public boolean delete(int codigo){
        Livro livro = new Livro(codigo);
        return livros.remove(livro);
    }

    public boolean update(Livro livro){
        int index = livros.indexOf(livro);
        if(index != -1){
            livros.set(index, livro);
            return true;
        }
        return false;
    }
}