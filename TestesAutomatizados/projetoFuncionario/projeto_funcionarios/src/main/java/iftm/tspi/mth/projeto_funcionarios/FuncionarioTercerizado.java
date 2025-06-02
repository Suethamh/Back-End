package iftm.tspi.mth.projeto_funcionarios;

public class FuncionarioTercerizado extends Funcionario {
    private Double despesasAdicionais;

    public FuncionarioTercerizado(String nome, Integer horasTrabalhadas, Double valorHora, Double despesasAdicionais) {
        super(nome, horasTrabalhadas, valorHora);
        this.despesasAdicionais = validarDespesasAdicionais(despesasAdicionais);
    }

    public FuncionarioTercerizado(Double despesasAdicionais) {
        this.despesasAdicionais = validarDespesasAdicionais(despesasAdicionais);
    }

    public Double getDespesasAdicionais() {
        return despesasAdicionais;
    }

    public void setDespesasAdicionais(Double despesasAdicionais) {
        this.despesasAdicionais = validarDespesasAdicionais(despesasAdicionais);
    }

    public Double validarDespesasAdicionais(Double despesasAdicional) throws IllegalArgumentException {
        if(despesasAdicional <= 0 || despesasAdicional > 1000) {
            throw new IllegalArgumentException("O valor para a despesas adicionais foi inválido. O valor tem que ser entre 0 e 1000.");
        }

        return despesasAdicional;
    }
}
