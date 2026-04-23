import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InversorTest {

    @Test
    void deveInverterPalavraNormal() {
        assertEquals("MTFI", Inversor.inverter("IFTM"));
    }

    @Test
    void deveRetornarStringVaziaParaEntradaVazia() {
        assertEquals("", Inversor.inverter(""));
    }

    @Test
    void deveManterStringDeUmUnicoCaractere() {
        assertEquals("A", Inversor.inverter("A"));
    }

    @Test
    void devePreservarPalindromo() {
        assertEquals("arara", Inversor.inverter("arara"));
    }
}
