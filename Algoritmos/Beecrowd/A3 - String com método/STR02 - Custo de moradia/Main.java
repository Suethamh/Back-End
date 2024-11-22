import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        double custo = ler.nextDouble();
        ler.nextLine();
        String cidade = ler.nextLine();

        if(cidade.equals("Uberlandia")){
            System.out.printf("O custo de vida em %s e R$ %.2f\n", cidade, custo);
        }else if(cidade.equals("Brasilia")){
            System.out.printf("O custo de vida em %s e R$ %.2f\n", cidade, custo*2);
        }else{
            System.out.printf("O custo de vida em %s e R$ %.2f\n", cidade, custo/2);
        }
    }
}