import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String data = ler.nextLine();

        String[] dias = data.split("/");

        System.out.println("Estamos no dia " + dias[0] + " do mês.");
    }
}