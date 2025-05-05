package iftm.tspi.mth.projeto_funcionarios;

public class Funcionario {
    private String nome;
    private Integer horasTrabalhadas;
    private Double valorHora;

    
    public Funcionario() {
    }
    

    public Funcionario(String nome, Integer horasTrabalhadas, Double valorHora) {
        this.nome = nome;
        this.horasTrabalhadas = horasTrabalhadas;
        this.valorHora = valorHora;
    }


    public Funcionario(Double valorHora) {
        this.valorHora = valorHora;
    }

    public Funcionario(Integer horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public Funcionario(Integer horasTrabalhadas, Double valorHora) {
        this.horasTrabalhadas = horasTrabalhadas;
        this.valorHora = valorHora;
    }

    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }


    public Integer getHorasTrabalhadas() {
        throw new UnsupportedOperationException("Método nao Existe");
    }


    public void setHorasTrabalhadas(Integer horasTrabalhadas) {
        throw new UnsupportedOperationException("Método nao Existe");
    }


    public Double getValorHora() {
        throw new UnsupportedOperationException("Método nao Existe");
    }


    public void setValorHora(Double valorHora) {
        throw new UnsupportedOperationException("Método nao Existe");
    }
    
    
    public Double calcularPagamento(){
        throw new UnsupportedOperationException("Método nao Existe");
    }
}
