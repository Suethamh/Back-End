package etapa02;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        HashSet<Integer> set1 = new HashSet<>();
        HashSet<Integer> set2 = new HashSet<>();
        HashSet<Integer> set3 = new HashSet<>();

        int temp = 0;
        while (true) {
            temp = ler.nextInt();
            if (temp < 0) {
                break;
            }
            set1.add(temp);
        }

        do {
            temp = ler.nextInt();
            
            if (temp < 0) {
                break;
            }

            set2.add(temp);

            if (!set1.add(temp)){
                set3.add(temp);
            }

        }while(temp > 0);

        for (Integer i : set3) {
            System.out.println(i);
        }

    }
}
