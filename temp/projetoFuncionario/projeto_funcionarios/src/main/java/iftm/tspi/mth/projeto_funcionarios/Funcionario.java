package iftm.tspi.mth.projeto_funcionarios;

public class Funcionario {
    private String nome;
    private Integer horasTrabalhadas;
    private Double valorHora;

    
    public Funcionario() {
    }
    

    public Funcionario(String nome, Integer horasTrabalhadas, Double valorHora) {
        this.nome = nome;
        this.horasTrabalhadas = validaHorasTrabalhadas(horasTrabalhadas);
        this.valorHora = validaValorHora(valorHora);
    }


    public Funcionario(Double valorHora) {
        this.valorHora = validaValorHora(valorHora);
    }

    public Funcionario(Integer horasTrabalhadas) {
        this.horasTrabalhadas = validaHorasTrabalhadas(horasTrabalhadas);
    }

    public Funcionario(Integer horasTrabalhadas, Double valorHora) {
        this.horasTrabalhadas = validaHorasTrabalhadas(horasTrabalhadas);
        this.valorHora = validaValorHora(valorHora);
    }

    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }


    public Integer getHorasTrabalhadas() {
        return horasTrabalhadas;
    }


    public void setHorasTrabalhadas(Integer horasTrabalhadas) {
        this.horasTrabalhadas = validaHorasTrabalhadas(horasTrabalhadas);
    }


    public Double getValorHora() {
        return valorHora;
    }


    public void setValorHora(Double valorHora) {
        this.valorHora = validaValorHora(valorHora);
    }
    
    
    public Double calcularPagamento(){
        Double resultado = valorHora * horasTrabalhadas;
        if(resultado > 6072 || resultado < 1518){
            throw new IllegalArgumentException("O valor do pagamento precisa ser maior que o salário mínimo (R$1.518,00) e menor que R$6.072,00");
        }
        return valorHora * horasTrabalhadas;
    }

    public Integer validaHorasTrabalhadas(Integer horasTrabalhadas){
        if(horasTrabalhadas > 40 || horasTrabalhadas < 20){
            throw new IllegalArgumentException("Número de horas trabalhadas inválida. Precisa ser entre 20 e 40.");
        }else{
            return horasTrabalhadas;
        }
    }

    public Double validaValorHora(Double valorHora){
        if(valorHora > 151.8 || valorHora < 68.72){
            throw new IllegalArgumentException("O valor está fora do esperado: entre 4% e 10% do salário mínimo (1518.00)");
        }else{
            return valorHora;
        }
    }
}
