package etapa07;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<Integer, String> map2 = new HashMap<>();

        String letra;
        int num;

        while(true){
            letra = ler.next();
            if(letra.equalsIgnoreCase("fim")){
                break;
            }

            num = ler.nextInt();
            map.put(letra, num);
        }

        for(String i : map.keySet()){
            map2.put(map.get(i), i);
        }

        System.out.println(map2);
    }
}
