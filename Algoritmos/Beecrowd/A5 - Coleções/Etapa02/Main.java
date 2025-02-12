import java.util.ArrayList;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        List<String> nomes = new ArrayList<>();

        String nome;
        do{

            nome = ler.nextLine();

            if(!nome.equals("FIM")){
                nomes.add(nome);
            }

        }while(!nome.equals("FIM"));

        Collections.reverse(nomes);

        System.out.println();
        for(String x : nomes){
            System.out.println(x);
        }
    }
}