import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        List<Integer> numeros = new ArrayList<>();

        int n = 0;
        int soma = 0;
        do{
            soma += n;
            n = ler.nextInt();
            
            if(n>0){
                numeros.add(n);
            }
        }while(n > 0);

        System.out.println(numeros.size());
        System.out.println("soma = " + soma);
    }
}