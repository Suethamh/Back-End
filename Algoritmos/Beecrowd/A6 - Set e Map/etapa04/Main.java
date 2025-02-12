package etapa04;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        HashSet<String> nomes = new HashSet<>();
        String nome;
        String verifica;

        while (true) {
            nome = ler.nextLine();
            if(nome.equalsIgnoreCase("fim")){
                break;
            }
            nomes.add(nome);
        }

        verifica = ler.nextLine();

        if (nomes.contains(verifica)) {
            System.out.println("Existe");
        }else{
            System.out.println("Não existe");
        }

    }
}
