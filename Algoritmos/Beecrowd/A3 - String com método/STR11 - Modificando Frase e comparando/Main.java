import java.util.Scanner;
public class Main
{
	public static void main(String[] args) {
		Scanner ler = new Scanner(System.in);
		
		String texto = ler.nextLine();
		
		String semA = texto.replaceAll("A", "@");
		String modificado = semA.replaceAll("a", "@");
		
		String[] textoSeparado = modificado.split(" ");
		
		int diferenca = textoSeparado[0].compareTo(textoSeparado[textoSeparado.length - 1]);
		
		if(textoSeparado.length < 3){
		    System.out.println("Frase invalida.");
		}else if(diferenca < 0){
		    System.out.println(modificado);
		    System.out.println("palavra1 < palavra2");
		}else if(diferenca > 0){
		    System.out.println(modificado);
		    System.out.println("palavra1 > palavra2");
		}else{
		    System.out.println(modificado);
		    System.out.println("palavra1 == palavra2");
		}
	}
}
