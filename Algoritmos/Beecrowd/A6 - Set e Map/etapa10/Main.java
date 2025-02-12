package etapa10;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static String menu(){
        return "1 - Adicionar Aluno\n" +
        "2 - Adicionar uma nota a um aluno.\n" + 
        "3 - Calcular a média das notas de todos os alunos.\n" +
        "4 - Imprimir os dados dos alunso\n" +
        "5 - Sair";
    } 
    
    public static void adicionarAluno(Scanner ler, HashMap<String, Aluno> map){
        String matricula;
        String nome;
        String cpf;

        System.out.println("Digite a matricula do aluno: ");
        matricula = ler.nextLine();

        System.out.println("Digite o nome do aluno: ");
        nome = ler.nextLine();

        System.out.println("Digite o cpf do aluno: ");
        cpf = ler.nextLine();

        Aluno aluno = new Aluno(matricula, nome, cpf);

        map.put(matricula, aluno);
    }

    public static void adicionarNota(Scanner ler, HashMap<String, Aluno> map, HashMap<String, Double> notas){
        String matricula;
        double nota;

        System.out.println("Qual a matricula do aluno para inserir as notas?");
        matricula = ler.nextLine();

        System.out.println("Qual a nota do aluno: ");
        nota = ler.nextDouble();

        notas.put(matricula, nota);
    }

    public static void calcularMedia(HashMap<String, Aluno> map, HashMap<String, Double> notas){
        double soma = 0;

        for(double d : notas.values()){
            soma += d;
        }

        System.out.println("A média de todas as notas é: " + soma / notas.size());
    }

    public static void imprimirDados(HashMap<String, Aluno> map){
        for(String s : map.keySet()){
            System.out.println(map.get(s));
        }
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        HashMap<String, Aluno> map = new HashMap<>();
        HashMap<String, Double> notas = new HashMap<>();
        
        int opcao;
        do{
            System.out.println(menu());
            opcao = ler.nextInt();
            ler.nextLine();

            switch (opcao) {
                case 1:
                    adicionarAluno(ler, map);
                    System.out.println();
                    break;
                case 2:
                    adicionarNota(ler, map, notas);
                    System.out.println();
                    break;
                case 3:
                    calcularMedia(map, notas);
                    System.out.println();
                    break;
                case 4:
                    imprimirDados(map);
                    System.out.println();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }

        }while(opcao != 5);

    }
}
