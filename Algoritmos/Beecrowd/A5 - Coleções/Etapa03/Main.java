import java.util.*;    
    
public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int n = ler.nextInt();
        int x=0;
        int atual;
        int soma=0;
        int maior = Integer.MIN_VALUE;

        List<Integer> numeros = new ArrayList<>();
        
        for(int i=0; i<n;i++){
            x = ler.nextInt();

            numeros.add(x);
            atual = numeros.get(i);
            soma += atual;


            if(atual > maior){
                maior = atual;
            }

        }
        System.out.println();

        for(int y :numeros){
            System.out.printf("%d ", y);
        }
        
        System.out.println(soma);
        System.out.println(maior);
        numeros.removeIf(impar -> impar % 2 != 0);

        for(int y :numeros){
            System.out.printf("%d ", y);
        }
    }
}