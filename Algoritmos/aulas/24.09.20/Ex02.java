import java.util.Scanner;
public class Ex02 {

    public static void mediaNotas(double[] notas, int n) {
        double soma = 0;
        for(int i=0; i<n; i++) {
            soma += notas[i];
        }
        System.out.printf("A media das notas é: %.2f", soma/n);
    }

    public static double[] lerNotas(Scanner ler, int n) {
        double[] notas = new double[n];
        for(int i=0; i<n; i++) {
            notas[i] = ler.nextDouble();
        }
        return notas;
    }
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        System.out.println("quantas notas você quer digitar?");
        int n = ler.nextInt();
        
        double[] notas = lerNotas(ler, n);

        mediaNotas(notas, n);

        ler.close();
    }
}