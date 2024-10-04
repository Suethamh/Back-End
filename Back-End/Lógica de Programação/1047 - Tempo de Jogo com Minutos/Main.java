import java.util.*;
public class Main
{
	public static void main(String[] args) {
	    Scanner ler = new Scanner(System.in);
	    
	    int hi = ler.nextInt();
	    int mi = ler.nextInt();
	    int hf = ler.nextInt();
	    int mf = ler.nextInt();
	    
	    if(hi < hf && mi > mf){
	        int horEmMin = (hf - hi) *60;
	        int min = horEmMin % 60;
	        
	        mf += 60 + min;
	        mf -= mi;
	        
	        System.out.printf("O JOGO DUROU %d HORA(S) E %d MINUTO(S)\n", (minutos/60) - 1, mf);
	    }else if(hi < hf && mi < mf){
	        hf -= hi;
	        mf -= mi;
	        
	        System.out.printf("O JOGO DUROU %d HORA(S) E %d MINUTO(S)\n", hf, mf);
	    }else if(hi > hf && mi < mf){
	        hf = 24 - (hi-hf);
	        mf -= mi;
	        
	        System.out.printf("O JOGO DUROU %d HORA(S) E %d MINUTO(S)\n", hf, mf);
	    }else if(hi > hf && mi > mf){
	        hf = 23 - (hi-hf);
	        mf = 60 - (mi - mf);
	        
	        System.out.printf("O JOGO DUROU %d HORA(S) E %d MINUTO(S)\n", hf, mf);
	    }else if(hi == hf && mi < mf){
	        mf -= mi;
	        
	        System.out.printf("O JOGO DUROU 0 HORA(S) E %d MINUTO(S)\n", mf);
	        
	    }else if(hi == hf && mi > mf){
	        hf = 23 - (hi-hf);
	        mf = 60 - (mi-mf);
	        
	        System.out.printf("O JOGO DUROU %d HORA(S) E %d MINUTO(S)\n", hf, mf);
	        
	    }else if(hi < hf && mi == mf){
	        hf -= hi;
	        
	        System.out.printf("O JOGO DUROU %d HORA(S) E 0 MINUTO(S)\n", hf);
	        
	    }else{
	        System.out.printf("O JOGO DUROU 24 HORA(S) E 0 MINUTO(S)\n");
	    }
	    
	}
}
