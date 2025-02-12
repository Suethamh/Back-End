package etapa05;

import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static String menu(){
        return "1 - Adicionar Convidado\n" +
        "2 - Remover Convidado\n" + 
        "3 - Verificar na lista\n" +
        "4 - Exibir todos os convidados\n" +
        "5 - Quantidade de convidados\n" +
        "6 - Apagar todos os convidados\n" +
        "7 - Sair";
    }
    
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        HashSet<String> convidados = new HashSet<>();

        int opcao;
        do{
            System.out.println(menu());
            opcao = ler.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome do convidado: ");
                    convidados.add(ler.nextLine());
                    break;
                case 2:
                    System.out.println("Escreva o nome do convidado que deseja remover: ");
                    convidados.remove(ler.nextLine());
                    break;
                case 3:
                    System.out.println("Digite o nome para saber se está na lista: ");
                    if(convidados.contains(ler.nextLine())){
                        System.out.println("Está na lista");
                    }else{
                        System.out.println("Não está na lista");
                    }
                    break;
                case 4:
                    for(String s : convidados){
                        System.out.println("- " + s);
                    }
                    break;
                case 5:
                    System.out.println("Quantidade de convidados: " + convidados.size());
                    break;
                case 6:
                    System.out.println("Deseja mesmo apagar todos os convidados? (s/n)");
                    if(ler.nextLine().equalsIgnoreCase("s")){
                        convidados.clear();
                    }else{
                        System.out.println("Operação cancelada");
                    }
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }

        }while(opcao != 7);
    }
}
