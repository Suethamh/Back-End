import java.util.Scanner;

public class Ex01 {

    static double maior(double nota1, double nota2) {
        if (nota1 > nota2) {
            return nota1;
        } else {
            return nota2;
        }
    }
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        double nota1 = s.nextDouble();
        double nota2 = s.nextDouble();

        double maior = maior(nota1, nota2);

        System.out.println(maior);
        
        s.close();
    }
}