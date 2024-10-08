import java.util.Scanner;

public class Main {

    static int resultado = 0;

    public static void sequenciaSoma(int a, int b) {
        resultado += b;

        if (b >= a) {
            System.out.print(b + " ");
            System.out.printf("Sum=%d\n", resultado);
            resultado = 0;
            return;
        }

        System.out.printf("%d ", b);
        sequenciaSoma(a, b + 1);
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int a, b;
        do{
            a = ler.nextInt();
            b = ler.nextInt();

            if (b > a) {
                int temp = b;
                b = a;
                a = temp;
            }

            if(a > 0 && b > 0){
                sequenciaSoma(a, b);
            }

        }while(a > 0 && b > 0);
    }
}