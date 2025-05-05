package iftm.tspi.mth.projeto_funcionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FuncionarioTercerizadoTest {
    @Test
    public void testarFuncionarioTercerizadoConstrutorDespesasInvalido() {
        Double despesasAdicionaisInvalido = 10101010.10;
        String msgEsperada = "O valor para a despesas adicionais foi inválido. O valor tem que ser entre 0 e 1000.";

        FuncionarioTercerizado t1 = new FuncionarioTercerizado(despesasAdicionaisInvalido);
        Double saidaObtida = t1.getdespesasAdicionais();

        assertEquals(saidaObtida, msgEsperada);
    }
    
    @Test
    public void testarFuncionarioTercerizadoConstrutorDespesasMaiorValido() {
        Double despesasAdicionaisValido = 1000.00;
        Double saidaEsperada = despesasAdicionaisValido;

        FuncionarioTercerizado t1 = new FuncionarioTercerizado(despesasAdicionaisValido);
        Double saidaObtida = t1.getdespesasAdicionais();

        assertEquals(saidaObtida, saidaEsperada);
    }

    @Test
    public void testarFuncionarioTercerizadoConstrutorDespesasMenorValido() {
        Double despesasAdicionaisValido = 0.01;
        Double saidaEsperada = despesasAdicionaisValido;

        FuncionarioTercerizado t1 = new FuncionarioTercerizado(despesasAdicionaisValido);
        Double saidaObtida = t1.getdespesasAdicionais();

        assertEquals(saidaObtida, saidaEsperada);
    }

    @Test
    public void testarConstrutorEntradasValidas() {
        String nomeEsperado = "John Doe";
        Integer horasTrabalhadasEsperado = 20;    
        Double valorHoraEsperado = 14.5;    
        Double despesasAdicionaisEsperado = 999.99;

        FuncionarioTercerizado saidaObtida = new FuncionarioTercerizado(nomeEsperado, horasTrabalhadasEsperado, valorHoraEsperado, despesasAdicionaisEsperado);

        assertEquals(nomeEsperado, saidaObtida.getNome(), "Nome não corresponde");
        assertEquals(horasTrabalhadasEsperado, saidaObtida.getHorasTrabalhadas(), "Horas trabalhadas não corresponde");
        assertEquals(valorHoraEsperado, saidaObtida.getValorHora(), "Valor hora não corresponde");
        assertEquals(despesasAdicionaisEsperado, saidaObtida.getdespesasAdicionais(), "Despesas adicionais não corresponde");
    }

    @Test
    public void testarFuncionarioTercerizadoModificardespesasAdicionaisEntradaValida() {
        Double despesasAdicionaisValido = 1.00;
        Double despesasAdicionaisOriginal = 10.00;
        Double saidaEsperada = despesasAdicionaisValido;

        FuncionarioTercerizado saidaObtida = new FuncionarioTercerizado(despesasAdicionaisOriginal);
        saidaObtida.setdespesasAdicionais(despesasAdicionaisValido);

        assertEquals(saidaObtida.getdespesasAdicionais(), saidaEsperada);
    }
    @Test
    public void testarFuncionarioTercerizadoModificardespesasAdicionaisEntradaInvalida() {
        Double despesasAdicionaisInvalido = 1000.01;
        Double despesasAdicionaisOriginal = 10.00;
        String msgEsperada = "O valor para a despesas adicionais foi inválido. O valor tem que ser entre 0 e 1000.";

        FuncionarioTercerizado saidaObtida = new FuncionarioTercerizado(despesasAdicionaisOriginal);
        saidaObtida.setdespesasAdicionais(despesasAdicionaisInvalido);

        assertEquals(saidaObtida.getdespesasAdicionais(), msgEsperada);
    }
}