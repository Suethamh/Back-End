import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String frase = ler.nextLine();

        for(int i=0; i<frase.length(); i++){
            System.out.printf("%d - %s\n", i, frase.charAt(i));
        }
    }
}