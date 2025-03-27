public class ContaBancaria {
    protected String titular;
    protected double saldo;

    public ContaBancaria() {
    }

    public ContaBancaria(String titular, double saldo) {
        this.titular = titular;
        this.saldo = saldo;
    }

    public void depositar(double qtd) {
        saldo += qtd;
    }

    public boolean sacar(double qtd) throws MinhasExcecoes{
        if(saldo >= qtd) {
            saldo -= qtd;
            return true;
        } else {
            throw new MinhasExcecoes("Saldo insuficiente");
        }
    }

    public String exibeSaldo() {
        return "Saldo da conta de " +
        titular + ": R$ "+
        String.format("%.2f", saldo);
    }
}
