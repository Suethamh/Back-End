import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String texto = ler.nextLine();

        if(texto.length() < 5){
            System.out.println("invalida");
        }else{
            if(texto.charAt(0) == texto.charAt(texto.length()-1)){
                System.out.println("iguais");
            }else{
                System.out.println("diferentes");
            }
        }
    }
}