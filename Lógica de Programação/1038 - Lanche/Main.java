import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int x = ler.nextInt();
        double y = ler.nextDouble();

        if (x == 1) {
            y = y*4.00;
            System.out.printf("Total: R$ %.2f\n", y);
            
        } else if (x == 2) {
            y = y*4.50;
            System.out.printf("Total: R$ %.2f\n", y);
            
        } else if (x == 3) {
            y = y*5.00;
            System.out.printf("Total: R$ %.2f\n", y);
            
        } else if (x == 4) {
            y = y*2.00;
            System.out.printf("Total: R$ %.2f\n", y);
            
        } else {
            y = y*1.50;
            System.out.printf("Total: R$ %.2f\n", y);
            
        }

        ler.close();
    }
}