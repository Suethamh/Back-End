import java.util.*;
public class Main
{
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		double num = s.nextDouble();
		double soma = 0;
		int total = 0;
		
		while(num >= 0.0){
		    soma += num;
		    total ++;
		    
		    num = s.nextDouble();
		}
		
		
		System.out.printf("Soma = %.2f\n", soma);
		System.out.printf("Quantidade = %d\n", total);
		
	}
}
