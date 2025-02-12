import java.util.Objects;

public class Aluno {

    public String matricula;
    public String nome;

    public Aluno() {
    }

    public Aluno(String matricula, String nome) {
        this.matricula = matricula;
        this.nome = nome;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return matricula.equals(((Aluno) o).getMatricula());
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }

}