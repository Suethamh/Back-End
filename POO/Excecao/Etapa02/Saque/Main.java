package Saque;

import java.util.Scanner;

public class Main {

    public static void limiteSaque(double saque){
        if(saque > 2000){
            throw new IllegalArgumentException("Saque indisponível para valores maiores que R$2000,00");
        }else {
            System.out.println("valor de R$" + saque + " sacado");
        }
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        double saque;

        while(true){
            try{
                System.out.println("Quanto deseja sacar?");
                saque = ler.nextDouble();
    
                limiteSaque(saque);
                break;
            }catch(IllegalArgumentException ex){
                System.err.println(ex.getMessage());
            }

            ler.nextLine();
        }

        
    }
}
