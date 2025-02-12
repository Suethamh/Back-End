package Etapa04;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static String Menu(){
        return """
                O que deseja fazer?
                1 - adicionar a lista
                2 - verificar se nome existe
                """;
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        List<String> convidados = new ArrayList<>();

        String convidado = "";
        boolean existe;

        System.out.println("O que você quer fazer? 1 - Adicionar, 2 - verificar convidado");

        do{
            existe = false;
            System.out.println("Quem você deseja incluir na lista de conviados?");
            convidado = ler.nextLine();


            for(String pessoa : convidados){
                if (convidado.equals(pessoa)) {
                    System.out.println("convidado existe");
                    existe = true;
                    break;
                }
            }

            if(convidado.equals("fim")){
                break;
            }else if (!existe) {
                convidados.add(convidado);
            }

        }while(!convidado.equals("fim"));

        for(int i=0; i < convidados.size()-1; i++){
            System.out.print(convidados.get(i) + "-");
        }
        System.out.print(convidados.get(convidados.size()-1));
    }
}