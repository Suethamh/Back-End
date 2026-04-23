public interface Pilha<T> {
    void empilhar(T elemento);
    T desempilhar();
    T topo();
    boolean estaVazia();
    int tamanho();
}
