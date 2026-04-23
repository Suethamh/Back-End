import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PilhaComMinimoTest {

    @Test
    void minDeveAtualizarAposCadaPush() {
        PilhaComMinimo<Integer> pilha = new PilhaComMinimo<>();

        pilha.empilhar(5);
        assertEquals(5, pilha.min());

        pilha.empilhar(3);
        assertEquals(3, pilha.min());

        pilha.empilhar(7);
        assertEquals(3, pilha.min());

        pilha.empilhar(1);
        assertEquals(1, pilha.min());

        pilha.empilhar(4);
        assertEquals(1, pilha.min());
    }

    @Test
    void minDeveRestaurarAposPopDoMinimoAtual() {
        PilhaComMinimo<Integer> pilha = new PilhaComMinimo<>();
        pilha.empilhar(5);
        pilha.empilhar(3);
        pilha.empilhar(1);
        pilha.empilhar(4);
        assertEquals(1, pilha.min());

        assertEquals(4, pilha.desempilhar());
        assertEquals(1, pilha.min());

        assertEquals(1, pilha.desempilhar());
        assertEquals(3, pilha.min());

        assertEquals(3, pilha.desempilhar());
        assertEquals(5, pilha.min());
    }

    @Test
    void minDeveLidarComElementosDuplicadosNoMinimo() {
        PilhaComMinimo<Integer> pilha = new PilhaComMinimo<>();
        pilha.empilhar(2);
        pilha.empilhar(2);
        pilha.empilhar(5);
        assertEquals(2, pilha.min());

        pilha.desempilhar();
        assertEquals(2, pilha.min());

        pilha.desempilhar();
        assertEquals(2, pilha.min());

        pilha.desempilhar();
        assertTrue(pilha.estaVazia());
    }

    @Test
    void minDeveFuncionarComSequenciaDecrescente() {
        PilhaComMinimo<Integer> pilha = new PilhaComMinimo<>();
        pilha.empilhar(10);
        pilha.empilhar(8);
        pilha.empilhar(6);
        pilha.empilhar(4);

        assertEquals(4, pilha.min());
        pilha.desempilhar();
        assertEquals(6, pilha.min());
        pilha.desempilhar();
        assertEquals(8, pilha.min());
        pilha.desempilhar();
        assertEquals(10, pilha.min());
    }

    @Test
    void minDeveFuncionarComStrings() {
        PilhaComMinimo<String> pilha = new PilhaComMinimo<>();
        pilha.empilhar("banana");
        pilha.empilhar("abacate");
        pilha.empilhar("uva");

        assertEquals("abacate", pilha.min());
        pilha.desempilhar();
        assertEquals("abacate", pilha.min());
        pilha.desempilhar();
        assertEquals("banana", pilha.min());
    }
}
