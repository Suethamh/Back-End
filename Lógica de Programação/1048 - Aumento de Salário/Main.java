import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        double salario = ler.nextDouble();
        double aumento;
        
        if(salario >= 0 && salario <= 400){
            aumento = salario * 0.15;
            salario += aumento;
            
            System.out.printf("Novo salario: %.2f\n", salario);
            System.out.printf("Reajuste ganho: %.2f\n", aumento);
            System.out.printf("Em percentual: 15 %%\n");
            
        } else if(salario > 400 && salario <= 800){
            aumento = salario * 0.12;
            salario += aumento;
            
            System.out.printf("Novo salario: %.2f\n", salario);
            System.out.printf("Reajuste ganho: %.2f\n", aumento);
            System.out.printf("Em percentual: 12 %%\n");
            
        } else if(salario > 800 && salario <= 1200){
            aumento = salario * 0.1;
            salario += aumento;
            
            System.out.printf("Novo salario: %.2f\n", salario);
            System.out.printf("Reajuste ganho: %.2f\n", aumento);
            System.out.printf("Em percentual: 10 %%\n");
            
        } else if(salario > 1200 && salario <= 2000){
            aumento = salario * 0.07;
            salario += aumento;
            
            System.out.printf("Novo salario: %.2f\n", salario);
            System.out.printf("Reajuste ganho: %.2f\n", aumento);
            System.out.printf("Em percentual: 7 %%\n");
        } else if(salario > 2000){
            aumento = salario * 0.04;
            salario += aumento;
            
            System.out.printf("Novo salario: %.2f\n", salario);
            System.out.printf("Reajuste ganho: %.2f\n", aumento);
            System.out.printf("Em percentual: 4 %%\n");
        }
        

        ler.close();
    }
}