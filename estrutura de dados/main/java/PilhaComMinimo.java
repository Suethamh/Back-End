public class PilhaComMinimo<T extends Comparable<T>> {

    private final PilhaArray<T> principal = new PilhaArray<>();
    private final PilhaArray<T> minimos   = new PilhaArray<>();

    public void empilhar(T elemento) {
        principal.empilhar(elemento);
        if (minimos.estaVazia() || elemento.compareTo(minimos.topo()) <= 0) {
            minimos.empilhar(elemento);
        } else {
            minimos.empilhar(minimos.topo());
        }
    }

    public T desempilhar() {
        minimos.desempilhar();
        return principal.desempilhar();
    }

    public T min() {
        return minimos.topo();
    }

    public T topo() {
        return principal.topo();
    }

    public boolean estaVazia() {
        return principal.estaVazia();
    }

    public int tamanho() {
        return principal.tamanho();
    }
}
