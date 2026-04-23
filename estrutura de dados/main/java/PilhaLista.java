import java.util.NoSuchElementException;

public class PilhaLista<T> implements Pilha<T> {

    private static class No<T> {
        final T valor;
        No<T> proximo;
        No(T valor, No<T> proximo) {
            this.valor = valor;
            this.proximo = proximo;
        }
    }

    private No<T> topo;
    private int tamanho;

    @Override
    public void empilhar(T elemento) {
        topo = new No<>(elemento, topo);
        tamanho++;
    }

    @Override
    public T desempilhar() {
        if (topo == null) throw new NoSuchElementException("Pilha vazia");
        T valor = topo.valor;
        topo = topo.proximo;
        tamanho--;
        return valor;
    }

    @Override
    public T topo() {
        if (topo == null) throw new NoSuchElementException("Pilha vazia");
        return topo.valor;
    }

    @Override
    public boolean estaVazia() {
        return topo == null;
    }

    @Override
    public int tamanho() {
        return tamanho;
    }
}
