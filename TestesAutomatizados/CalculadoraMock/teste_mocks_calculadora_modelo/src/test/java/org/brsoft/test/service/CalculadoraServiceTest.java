package org.brsoft.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.InvalidParameterException;

import org.brsoft.entity.Calculadora;
import org.brsoft.service.CalculadoraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CalculadoraServiceTest {
    Double n1;
    Double n2;
    Double soma;
	
    @InjectMocks
    private CalculadoraService servico;

    @Mock
    private Calculadora calc;

    @BeforeEach
    public void configurarMock(){
        n1 = 3.0;
        n2 = 5.0;
        soma = 8.0;
        Mockito.when(calc.somar(n1, n2)).thenReturn(soma);
        Mockito.when(calc.somar(n2, n1)).thenReturn(n2);
        Mockito.when(calc.somar(n1, n1)).thenReturn(n1);
        Mockito.when(calc.somar(0, 0)).thenThrow(InvalidParameterException.class);
        Mockito.when(calc.somar(n1, 0)).thenThrow(InvalidParameterException.class);
        Mockito.when(calc.somar(0, n2)).thenThrow(InvalidParameterException.class);
    }

    @Test
    public void testarCaculoN1MaiorN2(){

        Double resultadoEsperado = 80.0;
        Double resultadoObtido = servico.calculo(n1, n2);

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void testarCaculoN2MaiorN1(){

        Double resultadoEsperado = 50.0;
        Double resultadoObtido = servico.calculo(n2, n1);

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void testarCaculoN1IgualN2(){

        Double resultadoEsperado = 30.0;
        Double resultadoObtido = servico.calculo(n1,n1);

        assertEquals(resultadoEsperado, resultadoObtido);
    }

    @Test
    public void testarCalculoN1IgualZero(){
        assertThrows(InvalidParameterException.class, () -> {servico.calculo(0, n2);});
    }

    @Test
    public void testarCalculoN2IgualZero(){
        assertThrows(InvalidParameterException.class, () -> {servico.calculo(n1, 0);});
    }

    @Test
    public void testarCalculoN1N2IgualZero(){
        assertThrows(InvalidParameterException.class, () -> {servico.calculo(0, 0);});
    }
}
