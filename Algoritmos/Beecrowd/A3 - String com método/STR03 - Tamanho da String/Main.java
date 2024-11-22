import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String texto = ler.nextLine();

        if(texto.length() <= 10){
            System.out.println("pequeno");
        }else{
            System.out.println("grande");
        }
    }
}