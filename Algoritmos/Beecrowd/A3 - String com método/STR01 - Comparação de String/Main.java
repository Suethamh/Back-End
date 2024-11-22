import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String t1 = ler.nextLine();
        String t2 = ler.nextLine();

        if(t1.equals(t2)){
            System.out.println("correto");
        }else{
            System.out.println("incorreto");
        }
    }
}