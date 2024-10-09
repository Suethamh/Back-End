import java.util.Scanner;

public class Main {

    public static double ler(int i) {
        Scanner ler = new Scanner(System.in);
        double x;
        if (i == 1) {
            System.out.println("Escreva o valor de 'a' que seja diferente de 0: ");
            x = ler.nextDouble();
        } else if(i == 2) {
            System.out.println("Escreva o valor de 'b': ");
            x = ler.nextDouble();
        } else {
            System.out.println("Escreva o valor de 'c': ");
            x = ler.nextDouble();
        }

        return x;
    }

    public static void exibe(double x1, double x2) {
        System.out.printf("A 1º raiz é: %.2f\n", x1);
        System.out.printf("A 2º raiz é: %.2f\n", x2);
    }

    public static void main(String[] args) {
        double a, b , c;

        do{
            a = ler(1);
        }while(a == 0);

        b = ler(2);
        c = ler(3);

        EGrau2 Eq = new EGrau2(a, b, c);

        double d = Eq.delta();

        if(d <= 0) {
            System.out.println("Não existem raizes reais para esses valores!");
        }else{
            double x1 = Eq.calculaX1(d);
            double x2 = Eq.calculaX2(d);
    
            exibe(x1, x2);
        }
    }
}