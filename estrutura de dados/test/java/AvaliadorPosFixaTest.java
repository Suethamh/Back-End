import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AvaliadorPosFixaTest {

    @Test
    void deveAvaliarSomaSimples() {
        assertEquals(7.0, AvaliadorPosFixa.avaliarPosFixa("3 4 +"));
    }

    @Test
    void deveAvaliarExpressaoComposta() {
        assertEquals(14.0, AvaliadorPosFixa.avaliarPosFixa("5 1 2 + 4 * + 3 -"));
    }

    @Test
    void deveAvaliarExpressaoComplexa() {
        assertEquals(5.0, AvaliadorPosFixa.avaliarPosFixa("15 7 1 1 + - / 3 * 2 1 1 + + -"));
    }

    @Test
    void deveLancarExcecaoAoDividirPorZero() {
        assertThrows(ArithmeticException.class,
                () -> AvaliadorPosFixa.avaliarPosFixa("4 0 /"));
    }

    @Test
    void deveCalcularModuloDeDoisInteiros() {
        assertEquals(1.0, AvaliadorPosFixa.avaliarPosFixa("10 3 %"));
    }

    @Test
    void deveLancarExcecaoAoCalcularModuloPorZero() {
        assertThrows(ArithmeticException.class,
                () -> AvaliadorPosFixa.avaliarPosFixa("7 0 %"));
    }
}
