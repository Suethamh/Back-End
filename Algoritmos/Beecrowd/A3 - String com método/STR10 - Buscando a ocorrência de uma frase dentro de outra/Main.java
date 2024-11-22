import java.util.Scanner;
public class Main
{
	public static void main(String[] args) {
		Scanner ler = new Scanner(System.in);
		
		String s1 = ler.nextLine();
		String s2 = ler.nextLine();
		
		
		int parte = s1.indexOf(s2);
		
		if(parte == -1){
		    System.out.println(s1);
		}else{
		    String corte = s1.substring(parte);
		    System.out.println(corte);
		    
		}
	}
}
