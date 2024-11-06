import java.util.Scanner;

public class App {
    
    public static JogadorDeFutebol lerDados(){
        Scanner ler = new Scanner(System.in);
        JogadorDeFutebol jogador = new JogadorDeFutebol();

        System.out.println("Qual o nome do jogador?");
        jogador.setNome(ler.nextLine());

        System.out.println("Qual a nacionalidade?");
        jogador.nacionalidade = ler.nextLine();

        System.out.println("Qual a data de nascimento?");
        jogador.nascimento = ler.nextInt();
        ler.nextLine();

        System.out.println("Qual a posição?");
        jogador.posicao = ler.nextLine();

        System.out.println("Qual o peso?");
        jogador.peso = ler.nextDouble();
        
        System.out.println("Qual a altura?");
        jogador.altura = ler.nextDouble();

        return jogador;
    }

    public static void tempoAposentadoria(JogadorDeFutebol jogador){
        int idade = jogador.calculaIdade();
        String posicao = jogador.posicao.toLowerCase();
        int aposentadoria = 0;

        switch (posicao) {
            case "atacante":
                aposentadoria = 35;
                break;
            case "defesa":
                aposentadoria = 40;
                break;
            case "meio de campo":
                aposentadoria = 38;
                break;
            default:
                break;
        }

        if(idade >= aposentadoria) {
            System.out.println("Já tá aposentado");
        }else {
            System.out.println("Faltam " + (aposentadoria-idade) + " anos para o jogador " + jogador.getNome() + " se aposentar.");
        }
    }


    public static void main(String[] args) {
        
        JogadorDeFutebol jogador = lerDados();

        jogador.exibe();
        tempoAposentadoria(jogador);
    }
}
