package etapa03;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        List<Integer> list1 = new ArrayList();
        Set<Integer> set1 = new HashSet<>();
        List<Integer> list2 = new ArrayList();
        int num;

        while (true) {
            if((num = ler.nextInt()) < 0){
                break;
            }
            list1.add(num);
        }

        for (Integer i : list1) {
            set1.add(i);
        }

        for(Integer i : set1){
            list2.add(i);
        }

        for (Integer i : list2) {
            System.out.println(i);
        }
        
    }
}
