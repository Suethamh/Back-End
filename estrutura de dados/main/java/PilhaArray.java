import java.util.NoSuchElementException;

public class PilhaArray<T> implements Pilha<T> {
    private Object[] dados;
    private int tamanho;

    public PilhaArray() {
        this.dados = new Object[16];
        this.tamanho = 0;
    }

    @Override
    public void empilhar(T elemento) {
        if (tamanho == dados.length) {
            Object[] novo = new Object[dados.length * 2];
            System.arraycopy(dados, 0, novo, 0, tamanho);
            dados = novo;
        }
        dados[tamanho++] = elemento;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T desempilhar() {
        if (estaVazia()) {
            throw new NoSuchElementException("Pilha vazia");
        }
        T elemento = (T) dados[--tamanho];
        dados[tamanho] = null;
        return elemento;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T topo() {
        if (estaVazia()) {
            throw new NoSuchElementException("Pilha vazia");
        }
        return (T) dados[tamanho - 1];
    }

    @Override
    public boolean estaVazia() {
        return tamanho == 0;
    }

    @Override
    public int tamanho() {
        return tamanho;
    }
}
