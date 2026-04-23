import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FilaDuasPilhasTest {

    @Test
    void devePreservarOrdemFIFO() {
        Fila<Integer> fila = new FilaDuasPilhas<>();
        fila.enqueue(1);
        fila.enqueue(2);
        fila.enqueue(3);

        assertEquals(1, fila.dequeue());
        assertEquals(2, fila.dequeue());
        assertEquals(3, fila.dequeue());
        assertTrue(fila.isEmpty());
    }

    @Test
    void deveLancarExcecaoAoRemoverDeFilaVazia() {
        Fila<Integer> fila = new FilaDuasPilhas<>();
        assertThrows(NoSuchElementException.class, fila::dequeue);
    }

    @Test
    void devePermitirIntercalarEnqueueEDequeue() {
        Fila<String> fila = new FilaDuasPilhas<>();
        fila.enqueue("a");
        fila.enqueue("b");
        assertEquals("a", fila.dequeue());

        fila.enqueue("c");
        assertEquals("b", fila.dequeue());
        assertEquals("c", fila.dequeue());
        assertTrue(fila.isEmpty());
    }

    @Test
    void peekNaoDeveRemoverElemento() {
        Fila<Integer> fila = new FilaDuasPilhas<>();
        fila.enqueue(10);
        fila.enqueue(20);

        assertEquals(10, fila.peek());
        assertEquals(2, fila.size());
        assertFalse(fila.isEmpty());
        assertEquals(10, fila.dequeue());
    }
}
