import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String t = ler.nextLine();
        String s = ler.nextLine();
        String[] palavras = t.split(" ");
        int cont = 0;
        for(int i=0; i<palavras.length; i++){
            if(palavras[i].contains(s)){
                cont++;
            }
        }

        System.out.println(cont);
    }
}