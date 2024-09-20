import java.util.Scanner;

public class Ex01{
    
    static double maiorNota(double nota1, double nota2) {
        if(nota1 > nota2) {
            return nota1;
        }else {
            return nota2;
        }
    }
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);

        double nota1 = sc.nextDouble();
        double nota2 = sc.nextDouble();

        double maior = maiorNota(nota1, nota2);

        System.out.println("A maior nota foi: " + maior);

        sc.close();
    }
}