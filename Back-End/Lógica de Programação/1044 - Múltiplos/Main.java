import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        int x = ler.nextInt();
        int y = ler.nextInt();
        
        if(y>x){
            if (y%x == 0) {
            System.out.println("Sao Multiplos");
            
            } else{
                System.out.println("Nao sao Multiplos");
            }
        } else{
            if (x%y == 0){
            System.out.println("Sao Multiplos");
            
            } else{
                System.out.println("Nao sao Multiplos");
            }
        }
        

        ler.close();
    }
}