package iftm.suetham.mil_vidas.domain;

public class Livro {
    private int cod_livro;
    private String titulo;
    private String escritor;
    private String editora;
    private String genero;
    private int num_paginas;
    private double avaliacao;

    public Livro() {
    }
    
    public Livro(int cod_livro) {
        this.cod_livro = cod_livro;
    }

    public Livro(int cod_livro, String titulo, String escritor, String editora, String genero, int num_paginas, double avaliacao) {
        this.cod_livro = cod_livro;
        this.titulo = titulo;
        this.escritor = escritor;
        this.editora = editora;
        this.genero = genero;
        this.num_paginas = num_paginas;
        this.avaliacao = avaliacao;
    }

    public int getCod_livro(){
        return cod_livro;
    }

    public void setCod_livro(int cod_livro){
        this.cod_livro = cod_livro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEscritor() {
        return escritor;
    }

    public void setEscritor(String escritor) {
        this.escritor = escritor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getNum_paginas() {
        return num_paginas;
    }

    public void setNum_paginas(int num_paginas) {
        this.num_paginas = num_paginas;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cod_livro;
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
        Livro other = (Livro) obj;
        if (cod_livro != other.cod_livro)
            return false;
        return true;
    }
    
}
