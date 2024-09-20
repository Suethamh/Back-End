import java.util.*;
public class Main
{
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		int lim = 0;
		double soma = 0;
		int num = 0, n = 0;
		int par = 0, impar = 0;
		
		
		do{
		    lim = s.nextInt();
		}while(lim <= 0);
		
		do{
		    num = s.nextInt();
		    
		    if(num % 2 == 0){
		        par++;
		        
		    }else{
		        impar++;
		    }
		    
		    soma += num;
		    n++;
		    
		}while(soma < lim);
		
		
		System.out.printf("Pares = %d\n", par);
		System.out.printf("Impares = %d\n", impar);
		System.out.printf("Media = %.2f\n", soma/n);
		
	}
}
