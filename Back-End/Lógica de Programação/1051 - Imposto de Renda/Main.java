import java.util.*;

public class Main{
    public static void main(String[] args){
        Scanner ler = new Scanner(System.in);
        
        double salario = ler.nextDouble();
        double total;
        
        double imposto1 = 1000 * 0.08;
        double imposto2 = 1500 * 0.18;
        
        if(salario <= 2000){
            System.out.println("Isento");
            
        }else if(salario > 2000 && salario <=3000){
            salario -= 2000;
            total = salario * 0.08;
            System.out.printf("R$ %.2f\n", total);
            
        }else if(salario > 3000 && salario <=4500){
            salario -= 3000;
            total = (salario * 0.18) + imposto1;
            System.out.printf("R$ %.2f\n", total);
            
        }else if(salario > 4500){
            salario -= 4500;
            total = (salario * 0.28) + imposto1 + imposto2;
            System.out.printf("R$ %.2f\n", total);
        }
        ler.close();
    }
}