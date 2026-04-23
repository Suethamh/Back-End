import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BufferCircularTest {

    @Test
    void bufferNovoDeveEstarVazio() {
        BufferCircular<String> buffer = new BufferCircular<>(3);
        assertTrue(buffer.estaVazio());
        assertFalse(buffer.estaCheio());
        assertEquals(0, buffer.tamanho());
    }

    @Test
    void deveManterOrdemFIFOAteEncher() {
        BufferCircular<Integer> buffer = new BufferCircular<>(3);
        buffer.adicionar(1);
        buffer.adicionar(2);
        buffer.adicionar(3);

        assertTrue(buffer.estaCheio());
        assertEquals(3, buffer.tamanho());

        assertEquals(1, buffer.removerMaisAntigo());
        assertEquals(2, buffer.removerMaisAntigo());
        assertEquals(3, buffer.removerMaisAntigo());
        assertTrue(buffer.estaVazio());
    }

    @Test
    void deveDescartarMaisAntigoQuandoCheio() {
        BufferCircular<String> logs = new BufferCircular<>(3);
        logs.adicionar("INFO: Servidor iniciado");
        logs.adicionar("INFO: Conexão aceita");
        logs.adicionar("WARN: Timeout na query");
        logs.adicionar("ERROR: Falha na autenticação");

        assertEquals(3, logs.tamanho());
        assertEquals("INFO: Conexão aceita", logs.removerMaisAntigo());
        assertEquals("WARN: Timeout na query", logs.removerMaisAntigo());
        assertEquals("ERROR: Falha na autenticação", logs.removerMaisAntigo());
    }

    @Test
    void deveLancarExcecaoAoRemoverDeBufferVazio() {
        BufferCircular<Integer> buffer = new BufferCircular<>(2);
        assertThrows(NoSuchElementException.class, buffer::removerMaisAntigo);
    }

    @Test
    void deveReutilizarPosicoesAposIntercalarAdicaoERemocao() {
        BufferCircular<Integer> buffer = new BufferCircular<>(3);
        buffer.adicionar(1);
        buffer.adicionar(2);
        assertEquals(1, buffer.removerMaisAntigo());

        buffer.adicionar(3);
        buffer.adicionar(4);
        buffer.adicionar(5);

        assertTrue(buffer.estaCheio());
        assertEquals(3, buffer.removerMaisAntigo());
        assertEquals(4, buffer.removerMaisAntigo());
        assertEquals(5, buffer.removerMaisAntigo());
    }

    @Test
    void deveSobrescreverMultiplasVezesMantendoSoOsUltimosN() {
        BufferCircular<Integer> buffer = new BufferCircular<>(3);
        for (int i = 1; i <= 10; i++) {
            buffer.adicionar(i);
        }

        assertEquals(3, buffer.tamanho());
        assertEquals(8, buffer.removerMaisAntigo());
        assertEquals(9, buffer.removerMaisAntigo());
        assertEquals(10, buffer.removerMaisAntigo());
    }

    @Test
    void deveRejeitarCapacidadeInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new BufferCircular<>(0));
        assertThrows(IllegalArgumentException.class, () -> new BufferCircular<>(-1));
    }
}
