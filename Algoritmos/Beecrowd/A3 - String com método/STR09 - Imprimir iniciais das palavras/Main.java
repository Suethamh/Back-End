import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String T = ler.nextLine();
        String[] palavras = T.split(" ");

        for(int i=0; i<palavras.length; i++){
            if(palavras[i].length() > 3){
                String corte = palavras[i].substring(0,3);
                System.out.println(corte);
            }
        }
    }
}