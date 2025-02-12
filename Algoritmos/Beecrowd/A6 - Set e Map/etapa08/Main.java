package etapa08;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static String menu(){
        return "1 - Adicionar Produto\n" +
        "2 - Buscar um produto pelo id\n" + 
        "3 - Remover um produto pelo id\n" +
        "4 - Listar todos os produtos\n" +
        "5 - Sair";
    }
    
    public static  void adicionarProduto(Scanner ler, HashMap<String, Produto> map){
        System.out.println("Digite o id do produto: ");
        String id = ler.next();
        ler.nextLine();

        System.out.println("Digite o nome do produto: ");
        String nome = ler.nextLine();

        Produto produto = new Produto(id, nome);
        map.put(id, produto);
    }

    public static void buscarPorId(Scanner ler, HashMap<String, Produto> map){
        System.out.println("Digite o id do produto: ");
        String id = ler.next();

        System.out.println(map.get(id));
    }

    public static void removerProduto(Scanner ler, HashMap<String, Produto> map){
        do{
            System.out.println("Digite o id do produto que deseja remover: ");
            String id = ler.next();

            if(map.remove(id) != null){
                System.out.println("Produto removido com sucesso");
                break;
                
            }else{
                System.out.println("Produto não encontrado");
            }

        }while(true);
    }

    public static void listarProdutos(HashMap<String, Produto> map){
        for(String i : map.keySet()){
            System.out.println(i + "- " + map.get(i));
        }
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        HashMap<String, Produto> map = new HashMap<>();

        int opcao;
        do{
            System.out.println(menu());
            opcao = ler.nextInt();

            switch (opcao) {
                case 1:
                    adicionarProduto(ler, map);
                    System.out.println();
                    break;
                case 2:
                    buscarPorId(ler, map);
                    System.out.println();
                    break;
                case 3:
                    removerProduto(ler, map);
                    System.out.println();
                    break;
                case 4:
                    listarProdutos(map);
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
