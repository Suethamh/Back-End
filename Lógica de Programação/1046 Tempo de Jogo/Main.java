import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int a = ler.nextInt();
        int b = ler.nextInt();
        int c;
        
        if(b<a){
            c = b + 24 - a;
            System.out.printf("O JOGO DUROU %d HORA(S)\n", c);
            
        }else if(a<b){
            c = b - a;
            System.out.printf("O JOGO DUROU %d HORA(S)\n", c);
            
        }else if (a == b){
            System.out.printf("O JOGO DUROU 24 HORA(S)\n");
        }
        

        ler.close();
    }
}