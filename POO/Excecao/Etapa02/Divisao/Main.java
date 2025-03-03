package Divisao;

public class Main {

    public static void Divisao(int num, int den) throws DivisaoNaoExata {
        if(num % 2 != 0){
            throw new DivisaoNaoExata(num, den);
        }else{
            System.out.println(num + "/" + den + "=" + (num/den));
        }
    }

    public static void main(String[] args) {
        int[] num = {4, 8, 5, 16, 32, 21, 64, 128, 62, 80, 90};
        int[] den = {2, 0, 4, 8, 0, 2, 4};

        for(int i=0; i<num.length; i++){
            try{
                Divisao(num[i], den[i]);
            }catch(DivisaoNaoExata ex){
                System.err.println(ex.toString());

            }catch(ArithmeticException ex1){
                System.err.println("Divisão por zero não existe");

            }catch(ArrayIndexOutOfBoundsException ex2){
                System.err.println(ex2.getMessage());
                
            }
        }
    }
}