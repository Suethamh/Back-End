public class ContaCoEmpresarial extends ContaCo{
    private double taxaJurosEmprestimo;

    public ContaCoEmpresarial(String titular, double saldo, double limiteChequeEspecial, double taxaJurosEmprestimo) {
        super(titular, saldo, limiteChequeEspecial);
        this.taxaJurosEmprestimo = taxaJurosEmprestimo;
    }

    public boolean solicitaEmprestimo(double qtd) throws MinhasExcecoes{
        if (qtd >= taxaJurosEmprestimo) {
            saldo += qtd;
            return true;
        } else {
            throw new MinhasExcecoes("Valor de empréstimo inválido");
        }
    }
    
    @Override
    public String exibeSaldo() {
        return "Saldo da conta empresarial de " +
        titular + ": R$ "+
        String.format("%.2f", saldo);
    }
}
