package iftm.tspi.mth.projeto_funcionarios;

public class FuncionarioTercerizado extends Funcionario {
    private Double despesasAdicionais;

    public FuncionarioTercerizado(String nome, Integer horasTrabalhadas, Double valorHora, Double despesasAdicionais) {
        super(nome, horasTrabalhadas, valorHora);
        this.despesasAdicionais = despesasAdicionais;
    }

    public FuncionarioTercerizado(Double despesasAdicionais) {
        this.despesasAdicionais = despesasAdicionais;
    }

    public Double getdespesasAdicionais() {
        throw new UnsupportedOperationException("Metodo ainda não implementado.");
    }

    public void setdespesasAdicionais(Double despesasAdicionais) {
        throw new UnsupportedOperationException("Metodo ainda não implementado.");
    }
}
