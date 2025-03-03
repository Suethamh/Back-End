// le 2 numero interios, 
// divide o primeiro pelo segundo e exibe o resultado. 
// Trate as duas excecoes possiveis de ocorrer

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static int divisao (int a, int b) {
        if(b == 0){
            throw new ArithmeticException("Divisões por 0 não podem ocorrer");
        }
        int resultado = a/b;
        return resultado;
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        int a, b;

        // while (true) {
        //     try{
        //         System.out.println("Digite o primeiro número inteiro..:");
        //         a = ler.nextInt();

        //         System.out.println("Digite o segundo número inteiro..:");
        //         b = ler.nextInt();

        //         System.out.println("\n" + a + " / " + b + " = " + (a/b));
        //         break;
        //     }catch(InputMismatchException ex){
        //         System.err.println("\nEsse valor não é válido\n\n");

        //     }catch(ArithmeticException ex){
        //         System.err.println("\nDivisões por 0 não podem ocorrer\n\n");

        //     }
        //     ler.nextLine();
        // }

        while (true) {
            try{
                System.out.println("Digite o primeiro número inteiro..:");
                a = ler.nextInt();

                System.out.println("Digite o segundo número inteiro..:");
                b = ler.nextInt();

                System.out.println("\n" + a + " / " + b + " = " + divisao(a, b));
                break;
            }catch(InputMismatchException ex){
                System.err.println("\nEsse valor não é válido\n\n");

            }catch(ArithmeticException ex){
                System.err.println(ex.getMessage());

            }
            ler.nextLine();
        }
    }
}