import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String cpf = ler.nextLine();

        if(cpf.length() == 11){
            System.out.println("valido");
        }else{
            System.out.println("invalido");
        }
    }
}