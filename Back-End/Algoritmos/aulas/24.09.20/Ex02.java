import java.util.Scanner;

public class Ex02 {
    static double mediaNotas(double[] notas, int n) {
        double total = 0;
        for(int i = 0; i < n; i++) {
            total += notas[i];
        }

        return total / n;
    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int n = s.nextInt();

        double[] notas = new double[n];
        
        for(int i = 0; i<n; i++) {
            notas[i] = s.nextDouble();
        }

        double total = mediaNotas(notas, n);

        System.out.println(total);
    }
}