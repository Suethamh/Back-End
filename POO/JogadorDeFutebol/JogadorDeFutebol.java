import java.util.Calendar;

public class JogadorDeFutebol {

    private String nome;
    public String nacionalidade;
    public int nascimento;
    public String posicao;
    public double peso;
    public double altura;

    public JogadorDeFutebol() {
    }

    public JogadorDeFutebol(String nome, String nacionalidade, int nascimento, String posicao, double peso, double altura) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.nascimento = nascimento;
        this.posicao = posicao;
        this.peso = peso;
        this.altura = altura;
    }

    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int calculaIdade() {
        int idade;

        Calendar data = Calendar.getInstance();
        int anoAtual = data.get(Calendar.YEAR);

        idade = anoAtual - (nascimento % 10000);
        return idade;
    }

    public String exibe() {
        int dia = nascimento / 1000000;
        int mes = (nascimento / 10000) % 100;
        int ano = nascimento % 10000;

        return 
        "Nome: " + nome + "\n" +
        "Data de Nascimento: " + String.format("%02d/%02d/%04d", dia, mes, ano) + "\n" +
        "Altura: " + String.format("%.2f", altura) + "m" + "\n" +
        "Peso: " + String.format("%.2f", peso) + "Kg" + "\n" +
        "Nacionalidade: " + nacionalidade + "\n" +
        "Posição: " + posicao;
    }
}