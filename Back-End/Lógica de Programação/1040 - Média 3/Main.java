import java.util.*;
public class Main
{
	public static void main(String[] args) {
	    Scanner ler = new Scanner(System.in);
	    float N1 = ler.nextFloat();
	    float N2 = ler.nextFloat();
	    float N3 = ler.nextFloat();
	    float N4 = ler.nextFloat();
	    float Ne;
	    float media = (N1*2+N2*3+N3*4+N4*1) / 10;
	    
	    System.out.printf("Media: %.1f\n", media);
	    
	    if(media >= 7.0){
	        System.out.println("Aluno aprovado.");
	        
	    }else if(media < 5.0){
	        System.out.println("Aluno reprovado.");
	        
	    }else if(media >= 5.0 && media<= 6.9){
	        Ne = ler.nextFloat();
	        System.out.println("Aluno em exame.");
	        System.out.printf("Nota do exame: %.1f\n", Ne);
	        if(Ne >= 5.0){
	            System.out.println("Aluno aprovado.");
	        }else {
	            System.out.println("Aluno reprovado.");
	        }
	        float Mf = (media + Ne) / 2;
	        System.out.printf("Media final: %.1f\n", Mf);
	    }
	    
	}
}
