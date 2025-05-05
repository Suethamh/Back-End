package iftm.tspi.mth.projeto_funcionarios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FuncionarioTests {

    @Test
    public void testarPagamentoInvalido(){
        int horasTrabalhadas = 41;
        double valorHora = 151.00;

        String saidaEsperada = "O Valor do Pagamento precisa ser menor que R$6.072,00";
        Funcionario funcionario = new Funcionario(horasTrabalhadas, valorHora);
        
        
        String saidaObtida = assertThrows(IllegalArgumentException.class, () -> {funcionario.calcularPagamento();}).getMessage();
        
        assertEquals(saidaEsperada, saidaObtida);
    }

    @Test
    public void testarPagamentoMenorValido(){
        int horasTrabalhadas = 25;
        double valorHora = 60.72;

        double saidaEsperada = 1518.0;

        Funcionario funcionario = new Funcionario(horasTrabalhadas, valorHora);
        
        double saidaObtida = funcionario.calcularPagamento();
        
        assertEquals(saidaEsperada, saidaObtida);
    }

    @Test
    public void testarPagamentoMaiorValido(){
        int horasTrabalhadas = 40;
        double valorHora = 151.8;

        double saidaEsperada = 6072;

        Funcionario funcionario = new Funcionario(horasTrabalhadas, valorHora);
        
        double saidaObtida = funcionario.calcularPagamento();

        assertEquals(saidaEsperada, saidaObtida);
    }

    @Test
    public void testarContrutorEntradaValorHoraMenorValida(){
        double valorHoraValida = 60.72;
        double valorEsperado = 60.72;

        Funcionario func = new Funcionario(valorHoraValida);
        
        double saidaObtida = func.getValorHora();
        
        assertEquals(valorEsperado, saidaObtida);
    }

    @Test
    public void testarContrutorEntradaValorHoraMaiorValida(){
        double valorHoraValida = 151.8;
        double valorEsperado = 151.8;

        Funcionario func = new Funcionario(valorHoraValida);
        
        double saidaObtida = func.getValorHora();
        
        assertEquals(valorEsperado, saidaObtida);
    }
    
    @Test
    public void testarContrutorEntradaValorHoraInvalida(){
        double valorHoraInvalido = 166.98;
        String mensagemEsperado = "O valor está fora do esperado: entre 4% e 10% do salário mínimo (1518.00)";

        String saidaObtida = assertThrows(IllegalArgumentException.class, () -> {new Funcionario(valorHoraInvalido);}).getMessage();
 
        assertEquals(saidaObtida, mensagemEsperado);
    }

    @Test
    public void testarContrutorEntradaHorasTrabalhadasMenorValida(){
        int horasTrabalhadasValido = 20;
        int valorEsperado = 20;

        Funcionario func = new Funcionario(horasTrabalhadasValido);
        
        int saidaObtida = func.getHorasTrabalhadas();
        
        assertEquals(valorEsperado, saidaObtida);
    }

    @Test
    public void testarContrutorEntradaHorasTrabalhadasMaiorValida(){
        int horasTrabalhadasValido = 40;
        int valorEsperado = 40;

        Funcionario func = new Funcionario(horasTrabalhadasValido);
        
        int saidaObtida = func.getHorasTrabalhadas();
        
        assertEquals(valorEsperado, saidaObtida);
    }

    @Test
    public void testarContrutorEntradaHorasTrabalhadasInvalido(){
        int horasTrabalhadasInvalido = 41;
        String saidaEsperada = "Número de horas trabalhadas inválida. Precisa ser entre 20 e 40.";
        
        String saidaObtida = assertThrows(IllegalArgumentException.class, () -> {new Funcionario(horasTrabalhadasInvalido);}).getMessage();
        
        assertEquals(saidaEsperada, saidaObtida);
    }

    @Test
    public void testarModificarHorasPagamentoInvalido(){
        Funcionario func = new Funcionario();
        func.setHorasTrabalhadas(20);
        func.setValorHora(68.72);

        String saidaEsperada = "O valor do pagamento precisa ser maior que o salário mínimo (R$1518.00)";

        String saidaObtida = assertThrows(IllegalArgumentException.class, () -> {func.calcularPagamento();}).getMessage();

        assertEquals(saidaEsperada, saidaObtida);
    }

    @Test
    public void testarModificarValorPagamentoInvalido(){
        Funcionario func = new Funcionario();
        func.setHorasTrabalhadas(20);
        func.setValorHora(75.9);

        String saidaEsperada = "O valor do pagamento precisa ser maior que o salário mínimo (R$1518.00)";

        String saidaObtida = assertThrows(IllegalArgumentException.class, () -> {func.calcularPagamento();}).getMessage();

        assertEquals(saidaEsperada, saidaObtida);
    }
}