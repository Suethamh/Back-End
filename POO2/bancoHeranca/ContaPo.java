public class ContaPo extends ContaBancaria {
    private double taxaRendimento;

    public ContaPo(String titular, double saldo, double taxaRendimento) {
        super(titular, saldo);
        this.taxaRendimento = taxaRendimento;
    }

    public void aplicarRendimento() {
        saldo += saldo * taxaRendimento/100;
    }

    @Override
    public String exibeSaldo() {
        return "Saldo da conta poupança de " +
        titular + ": R$ "+
        String.format("%.2f", saldo);
    }
}