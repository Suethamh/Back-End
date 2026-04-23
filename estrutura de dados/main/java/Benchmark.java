import java.util.ArrayDeque;

public class Benchmark {

    private static final int N = 1_000_000;
    private static final int REPETICOES = 3;

    public static void main(String[] args) {
        long[] tempoArray = new long[REPETICOES];
        long[] tempoLista = new long[REPETICOES];
        long[] tempoDeque = new long[REPETICOES];

        for (int r = 0; r < REPETICOES; r++) {
            tempoArray[r] = medir(() -> {
                PilhaArray<Integer> p = new PilhaArray<>();
                for (int i = 0; i < N; i++) { p.empilhar(i); p.desempilhar(); }
            });
            tempoLista[r] = medir(() -> {
                PilhaLista<Integer> p = new PilhaLista<>();
                for (int i = 0; i < N; i++) { p.empilhar(i); p.desempilhar(); }
            });
            tempoDeque[r] = medir(() -> {
                ArrayDeque<Integer> p = new ArrayDeque<>();
                for (int i = 0; i < N; i++) { p.push(i); p.pop(); }
            });
        }

        System.out.printf("Resultados para %d operações push+pop (média de %d execuções):%n%n",
                N, REPETICOES);
        System.out.printf("PilhaArray:  %7.2f ms  (execuções: %s)%n",
                media(tempoArray) / 1e6, formatar(tempoArray));
        System.out.printf("PilhaLista:  %7.2f ms  (execuções: %s)%n",
                media(tempoLista) / 1e6, formatar(tempoLista));
        System.out.printf("ArrayDeque:  %7.2f ms  (execuções: %s)%n",
                media(tempoDeque) / 1e6, formatar(tempoDeque));
    }

    static long medir(Runnable tarefa) {
        tarefa.run();
        long inicio = System.nanoTime();
        tarefa.run();
        return System.nanoTime() - inicio;
    }

    static double media(long[] valores) {
        long soma = 0;
        for (long v : valores) soma += v;
        return (double) soma / valores.length;
    }

    static String formatar(long[] valores) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.2f ms", valores[i] / 1e6));
        }
        return sb.append("]").toString();
    }
}
