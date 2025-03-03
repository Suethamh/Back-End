package Senha;

import java.util.Scanner;

public class Main {
    
    public static void senhaCorreta(String input) throws Exception{
        String senha = "teste123";
        
        if(!input.equals(senha)){
            throw new Exception("\nSenha Inválida\n\n");
        }
    }
    
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        String input;
        
        while (true) {
            try{
                System.out.println("Qual sua senha?");
                input = ler.nextLine();
    
                senhaCorreta(input);
                System.out.println("\nComputador da NASA invadido com sucesso\n");
                break;
            }catch(Exception ex){
                System.err.println(ex.getMessage());
            }
        }

        
    }
}
