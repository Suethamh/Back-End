import java.util.Scanner;

public class Main {

    public static int somatorio(int n) {
        if(n == 0) {
            return 0;
        }

        return (n % 10) + somatorio(n / 10);
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int n = ler.nextInt();

        do {
            n = somatorio(n);
        }while (n>9);
        
        System.out.println(n);
    }
}