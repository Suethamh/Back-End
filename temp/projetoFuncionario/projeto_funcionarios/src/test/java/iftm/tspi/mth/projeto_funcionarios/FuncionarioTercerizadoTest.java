package iftm.tspi.mth.projeto_funcionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FuncionarioTercerizadoTest {
    @Test
    public void testarFuncionarioTercerizadoConstrutorDespesasInvalido() {
        Double despesasAdicionaisInvalido = 10101010.10;
        String msgEsperada = "O valor para a despesas adicionais foi inválido. O valor tem que ser entre 0 e 1000.";

        String saidaObtida = assertThrows(IllegalArgumentException.class, () -> {
            FuncionarioTercerizado t1 = new FuncionarioTercerizado(despesasAdicionaisInvalido);
        }).getMessage();

        assertEquals(saidaObtida, msgEsperada);
    }
    
    @Test
    public void testarFuncionarioTercerizadoConstrutorDespesasMaiorValido() {
        Double despesasAdicionaisValido = 1000.00;
        Double saidaEsperada = despesasAdicionaisValido;

        FuncionarioTercerizado t1 = new FuncionarioTercerizado(despesasAdicionaisValido);
        Double saidaObtida = t1.getDespesasAdicionais();

        assertEquals(saidaObtida, saidaEsperada);
    }

    @Test
    public void testarFuncionarioTercerizadoConstrutorDespesasMenorValido() {
        Double despesasAdicionaisValido = 0.01;
        Double saidaEsperada = despesasAdicionaisValido;

        FuncionarioTercerizado t1 = new FuncionarioTercerizado(despesasAdicionaisValido);
        Double saidaObtida = t1.getDespesasAdicionais();

        assertEquals(saidaObtida, saidaEsperada);
    }

    @Test
    public void testarConstrutorEntradasValidas() {
        String nomeEsperado = "John Doe";
        Integer horasTrabalhadasEsperado = 20;    
        Double valorHoraEsperado = 75.9;    
        Double despesasAdicionaisEsperado = 999.99;

        FuncionarioTercerizado saidaObtida = new FuncionarioTercerizado(nomeEsperado, horasTrabalhadasEsperado, valorHoraEsperado, despesasAdicionaisEsperado);

        assertEquals(nomeEsperado, saidaObtida.getNome(), "Nome não corresponde");
        assertEquals(horasTrabalhadasEsperado, saidaObtida.getHorasTrabalhadas(), "Horas trabalhadas não corresponde");
        assertEquals(valorHoraEsperado, saidaObtida.getValorHora(), "Valor hora não corresponde");
        assertEquals(despesasAdicionaisEsperado, saidaObtida.getDespesasAdicionais(), "Despesas adicionais não corresponde");
    }

    @Test
    public void testarFuncionarioTercerizadoModificarDespesasAdicionaisEntradaValida() {
        Double despesasAdicionaisValido = 1.00;
        Double despesasAdicionaisOriginal = 10.00;
        Double saidaEsperada = despesasAdicionaisValido;

        FuncionarioTercerizado saidaObtida = new FuncionarioTercerizado(despesasAdicionaisOriginal);
        saidaObtida.setDespesasAdicionais(despesasAdicionaisValido);

        assertEquals(saidaObtida.getDespesasAdicionais(), saidaEsperada);
    }
    @Test
    public void testarFuncionarioTercerizadoModificarDespesasAdicionaisEntradaInvalida() {
        Double despesasAdicionaisInvalido = 1000.01;
        Double despesasAdicionaisOriginal = 10.00;
        String msgEsperada = "O valor para a despesas adicionais foi inválido. O valor tem que ser entre 0 e 1000.";

        FuncionarioTercerizado f1 = new FuncionarioTercerizado(despesasAdicionaisOriginal);
        String saidaObtida = assertThrows(IllegalArgumentException.class, () -> {
            f1.setDespesasAdicionais(despesasAdicionaisInvalido);
        }).getMessage();

        assertEquals(saidaObtida, msgEsperada);
    }
}
