package org.iftm.atividadea2.calculadora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.iftm.atividadea2.Calculadora;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculadoraTest {
    private Calculadora calc;

    @Test
	public void testarConstrutorVazio(){
        //arrange
		Calculadora calculadora = new Calculadora();
		int resultadoEsperado = 0;
		int resultadoObtido = calculadora.getMemoria();

        //act
		assertEquals(resultadoEsperado, resultadoObtido);
	}

	@Test
	public void TestarConstrutorComValorPositivo(){
        //arrange
        Calculadora calculadora = new Calculadora(3);
        int resultadoEsperado = 3;
        int resultadoObtido = calculadora.getMemoria();

        //act
        assertEquals(resultadoEsperado, resultadoObtido);
	}

    @Test
    public void TestarConstrutorComValorNegativo(){
        //arrange
        Calculadora calculadora = new Calculadora(-3);
        int resultadoEsperado = -3;
        int resultadoObtido = calculadora.getMemoria();

        //act
        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @BeforeEach
    public void incializarCenario(){
        calc = new Calculadora(3);
    }

    @AfterEach
    public void finalizarCenario(){
        calc.zerarMemoria();
    }


    @Test
    public void TestarSomaComValorPositivo(){
        //arrange
        int valorTeste = 3;
        int resultadoEsperado = 6;
        
        calc.somar(valorTeste);

        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void TestarSomaComValorNegativo(){
        int valorTeste = -3;
        int resultadoEsperado = 0;
        
        calc.somar(valorTeste);
        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void TestarSubtracaoComValorPositivo(){
        int valorTeste = 3;
        int resultadoEsperado = 0;

        calc.subtrair(valorTeste);
        int resultadoObtido = calc.getMemoria();
        assertEquals(resultadoEsperado, resultadoObtido);

    }

    @Test
    public void TestarSubtracaoComValorNegativo(){
        int valorTeste = -3;
        int resultadoEsperado = 6;

        calc.subtrair(valorTeste);
        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
    }
    
    @Test
    public void TestarMultiplicacaoComValorPositivo(){
        int valorTeste = 3;
        int resultadoEsperado = 9;

        calc.multiplicar(valorTeste);
        int resultadoObtido = calc.getMemoria();
        assertEquals(resultadoEsperado, resultadoObtido);

    }

    @Test
    public void TestarMultiplicacaoComValorNegativo(){
        int valorTeste = -3;
        int resultadoEsperado = -9;

        calc.multiplicar(valorTeste);
        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void TestarDivisaoComValorZeroRetornaErro(){
        int valorTeste = 0;
        String mensagemEsperada = "Divisão por zero!!!";

        Exception resultadoObtido = assertThrows(Exception.class, () -> {
            calc.dividir(valorTeste);
        });

        assertEquals(mensagemEsperada, resultadoObtido.getMessage());

        
    }

    @Test
    public void TestarDivisaoComValorPositivo() throws Exception{
        int valorTeste = 3;
        double resultadoEsperado = 1;
        

        calc.dividir(valorTeste);
        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void TestarDivisaoComValorNegativo() throws Exception{
        int valorTeste = -3;
        double resultadoEsperado = -1;

        calc.dividir(valorTeste);
        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void TestarExponenciacaoComValorUm() throws Exception{
        int valorTeste = 1;
        int resultadoEsperado = 3;

        calc.exponenciar(valorTeste, calc.getMemoria());

        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
        
    }

    @Test
    public void TestarExponenciacaoComValorDez() throws Exception{
        int valorTeste = 10;
        double resultadoEsperado = 59049;
        

        calc.exponenciar(valorTeste, calc.getMemoria());
        int resultadoObtido = calc.getMemoria();

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void TestarExponenciacaoComValorVinte(){
        int valorTeste = 20;
        String mensagemEsperada = "Expoente incorreto, valor máximo é 10.";

        Exception mensagemObtida = assertThrows(Exception.class, () -> calc.exponenciar(valorTeste, calc.getMemoria()));

        assertEquals(mensagemEsperada, mensagemObtida.getMessage());
    }
}
