package org.iftm.atividadea2;
//há erros
public class Calculadora {
    private int memoria;

    public Calculadora() {
        this.memoria = 0;
    }

    public Calculadora(int memoria) {
        this.memoria = memoria;
    }

    public int getMemoria() {
        return this.memoria;
    }

    public void zerarMemoria() {
        this.memoria = 0;
    }

    public void somar(int valor) {
        this.memoria += valor;
    }

    public void subtrair(int valor) {
        this.memoria -= valor;
    }

    public void multiplicar(int valor) {
        this.memoria *= valor;
    }

    public void dividir(int valor) throws Exception {
        if (valor == 0)
            throw new Exception("Divisão por zero!!!");
        this.memoria /= valor;
    }

    public void exponenciar(int valor, int memoriaInicial) throws Exception {
        if (valor > 10)
            throw new Exception("Expoente incorreto, valor máximo é 10.");
        for (int i = 1; i < valor; i++) {
            this.memoria *= memoriaInicial;
        }
    }
}
