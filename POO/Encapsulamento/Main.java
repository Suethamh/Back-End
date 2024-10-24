import java.util.Scanner;

public class Main {
    
    public static Pessoa lerPessoa() {
        Scanner ler = new Scanner(System.in);
        Pessoa p1 = new Pessoa();

        System.out.println("Qual o seu nome?");
        p1.setNome(ler.nextLine());

        System.out.println("Qual o seu sexo?");
        p1.setSexo(ler.next().charAt(0));
        ler.nextLine();

        System.out.println("Qual sua idade?");
        p1.setIdade(ler.nextInt());

        return p1;
    }

    public static Formacao lerFormacao() {
        Scanner ler = new Scanner(System.in);
        char afirmacao;
        Formacao f1 = new Formacao();

        System.out.println("Qual seu nível de escolaridade?");
        f1.setNivel(ler.nextLine());

        System.out.println("Você já concluiu ele? (s/n)");
        afirmacao = ler.next().charAt(0);
        if(afirmacao == 's' || afirmacao == 'S') {
            f1.setConcluido(true);
        } else {
            f1.setConcluido(false);
        }
        ler.nextLine();

        System.out.println("Em qual instituição você estudou/estuda?");
        f1.setInstituicao(ler.nextLine());

        System.out.println("Em qual ano está previsto para terminar?");
        f1.setAno(ler.nextInt());
        ler.nextLine();

        return f1;
    }

    public static String exibePessoa(Pessoa p1) {
        return "Nome: " + p1.getNome() + "\n" +
               "Sexo: " + p1.getSexo() + "\n" +
               "Idade: " + p1.getIdade() + "\n";
    }
    
    public static String exibeFormacao(Formacao f1) {
        return "Nível de escolaridade: " + f1.getNivel() + "\n" +
               "Concluído: " + f1.isConcluido() + "\n" +
               "Instituição: " + f1.getInstituicao() + "\n" +
               "Ano: " + f1.getAno() + "\n";
    

    public static void Exibir(Pessoa p1, Formacao f1) {
        System.out.println();
        System.out.println(exibePessoa(p1));
        System.out.println(exibeFormacao(f1));
    }

    public static void atualizarIdade(Pessoa p1) {
        Scanner ler = new Scanner(System.in);
        System.out.println("Qual a nova idade de " + p1.getNome() + "?");
        p1.setIdade(ler.nextInt());
    }

    public static int exibirMenu() {
        Scanner ler = new Scanner(System.in);

        System.out.println("\nEscolha o que você quer fazer:");
        System.out.println("1 - Cadastrar Pessoa");
        System.out.println("2 - Cadastrar Formação");
        System.out.println("3 - Exibir");
        System.out.println("4 - Atualizar idade");
        System.out.println("5 - Sair");


        return ler.nextInt();
    }

    
    public static void main(String[] args) {

        Pessoa p1 = new Pessoa();
        Formacao f1 = new Formacao();
        int opcao;

        do{
            opcao = exibirMenu();
            if(opcao > 5 || opcao < 1){
                
            }

            switch (opcao) {
                case 1:
                    p1 = lerPessoa();
                    break;
                case 2:
                    f1 = lerFormacao();
                    break;
                case 3:
                    Exibir(p1, f1);
                    break;
                case 4:
                    atualizarIdade(p1);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("digite uma opção válida");
                    break;
            }
        }while(opcao != 5);

    }
}