import java.util.Scanner;

public class Main {

    static int soma = 0;

    public static void somaImpares(int n, int i) {
        if(n == 0) {
            System.err.println("0 = 0");
            return;
        }

        if(i == n || i == n-1) {
            soma += i;
            System.out.printf("%d = %d\n", i, soma);
            return;
        }

        soma += i;
        System.out.printf("%d + ", i);

        somaImpares(n, i+2);
        
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int n = ler.nextInt();

        somaImpares(n, 1);
    }
}