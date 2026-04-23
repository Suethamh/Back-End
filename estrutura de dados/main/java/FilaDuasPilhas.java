import java.util.NoSuchElementException;

public class FilaDuasPilhas<T> implements Fila<T> {

    private final PilhaArray<T> entrada = new PilhaArray<>();
    private final PilhaArray<T> saida   = new PilhaArray<>();

    @Override
    public void enqueue(T elemento) {
        entrada.empilhar(elemento);
    }

    @Override
    public T dequeue() {
        transferirSeNecessario();
        if (saida.estaVazia()) throw new NoSuchElementException();
        return saida.desempilhar();
    }

    @Override
    public T peek() {
        transferirSeNecessario();
        if (saida.estaVazia()) throw new NoSuchElementException();
        return saida.topo();
    }

    @Override
    public boolean isEmpty() {
        return entrada.estaVazia() && saida.estaVazia();
    }

    @Override
    public int size() {
        return entrada.tamanho() + saida.tamanho();
    }

    private void transferirSeNecessario() {
        if (saida.estaVazia()) {
            while (!entrada.estaVazia()) {
                saida.empilhar(entrada.desempilhar());
            }
        }
    }
}
