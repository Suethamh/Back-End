public class Inversor {

    public static String inverter(String texto) {
        Pilha<Character> pilha = new PilhaArray<>();

        for (int i = 0; i < texto.length(); i++) {
            pilha.empilhar(texto.charAt(i));
        }

        String resultado = "";
        while (!pilha.estaVazia()) {
            resultado += pilha.desempilhar();
        }
        return resultado;
    }
}
