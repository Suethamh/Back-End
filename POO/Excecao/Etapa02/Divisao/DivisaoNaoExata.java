package Divisao;

public class DivisaoNaoExata extends Exception{
    int num;
    int den;

    public DivisaoNaoExata(int num, int den){
        super();
        this.num = num;
        this.den = den;
    }

    @Override
    public String toString(){
        return "A divisao não dá um valor exato";
    }
}
