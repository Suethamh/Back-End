package etapa10;

import java.util.Objects;

public class Aluno {
    
    public String matricula;
    public String nome;
    public String cpf;

    public Aluno() {
    }

    public Aluno(String matricula, String nome, String cpf) {
        this.matricula = matricula;
        this.nome = nome;
        this.cpf = cpf;
    }

    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() == o.getClass()) return false;
        return matricula.equals(((Aluno) o).getMatricula());
    }

    @Override
    public int hashCode(){
        return Objects.hash(matricula);
    }

    @Override
    public String toString(){
        return "Aluno [matricula: " + matricula +", nome: "+ nome +", cpf: "+cpf +" ]";
    }
}
