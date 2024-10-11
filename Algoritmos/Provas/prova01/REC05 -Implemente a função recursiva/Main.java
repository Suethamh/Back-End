import java.util.Scanner;

public class Main {

    public static int h(int m, int n) {
        if(n==1) {
            return m+1;
        }
        if(m==1){
            return n+1;
        }

        return h(m, n-1) + h(m-1, n);
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int m = ler.nextInt();
        int n = ler.nextInt();

        int valor = h(m,n);

        System.out.printf("h(%d,%d) = %d\n", m, n, valor);
    }
}