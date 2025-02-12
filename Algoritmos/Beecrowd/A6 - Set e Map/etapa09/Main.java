package etapa09;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        HashMap<Character, Integer> map = new HashMap<>();

        String palavra = ler.nextLine();
        String[] palavrasString = palavra.split(" ");

        List<Character> letras = new ArrayList<>();

        for (String palavraAtual : palavrasString) {
            String[] letrasString = palavraAtual.split("");
            
            for (String letra : letrasString) {
                letras.add(letra.charAt(0));
            }
        }
        Character[] arrayLetras = letras.toArray(new Character[0]);

        for(Character i : arrayLetras){
            if(!map.containsKey(i)){
                map.put(i, 1);
            }
            else{
                map.put(i, map.get(i) + 1);
            }

        }
        System.out.println(map);
    }
}
