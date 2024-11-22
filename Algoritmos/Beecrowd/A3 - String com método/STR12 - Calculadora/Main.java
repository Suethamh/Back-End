import java.util.Scanner;
public class Main
{
	public static void main(String[] args) {
		Scanner ler = new Scanner(System.in);
		
		String formula = ler.nextLine();
		
		String[] formulaSeparada = formula.split(" ");
		String operador = formulaSeparada[1];
		
		double primeiroNum = Double.parseDouble(formulaSeparada[0]);
		double segundoNum = Double.parseDouble(formulaSeparada[2]);
		
		double resultado = 0;
		
		switch(operador){
		    case "+":
		        resultado = primeiroNum + segundoNum;
		        break;
		    case "-":
		        resultado = primeiroNum - segundoNum;
		        break;
		    case "*":
		        resultado = primeiroNum * segundoNum;
		        break;
		    case "/":
		        resultado = primeiroNum / segundoNum;
		        break;
		}
		
		if(formulaSeparada.length < 3){
		    System.out.println("Formula invalida.");
		}else{
		    System.out.printf("%.1f\n", resultado);
		}
	}
}
