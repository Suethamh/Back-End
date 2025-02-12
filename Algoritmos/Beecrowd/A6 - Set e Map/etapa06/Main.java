package etapa06;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        HashMap<Integer, Integer> map = new HashMap<>();

        int num;
        while (true) {
            if ((num = ler.nextInt()) < 0) {
                break;
            }
            if(!map.containsKey(num)){
                map.put(num, 1);
            }else{
                map.put(num, map.get(num) + 1);
            }
        }

        for (Integer i : map.keySet()) {
            System.out.println(i + ": " + map.get(i));
        }
        
    }
}
