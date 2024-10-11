import java.util.Scanner;

public class Main {

//     private static int funcaoFF(int n) {
//         if (n == 1){
//             return funcaoFF(n-1);
//         }else if (n % 2 == 0){
//             return funcaoFF(n/2);
//         }else {
//             return funcaoFF((n-1)/2) + funcaoFF((n+1)/2);
//         }      
//     }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        int n = s.nextInt();
        System.out.println("ff(" + n + ") = " + funcaoFF(n));
        s.close();
    }

// Na linha 5 trocaria o private por public, para o main poder acessar a função.

// A função recursiva acima não possui um caso base de retorno, onde não chamaria a função novamente.
// linhas 7, 9 e 11.

// Na linha 6 colocaria um caso para que numeros negativos também fossem considerados

    public static int funcaoFF(int n) {
        if (n == 1 || n < 0){
            return 1;
        }else if (n % 2 == 0){
            return funcaoFF(n/2);
        }else {
            return funcaoFF((n-1)/2) + funcaoFF((n+1)/2);
        }      
    }

}