public class AvaliadorPosFixa {

    public static double avaliarPosFixa(String expressao) {
        Pilha<Double> pilha = new PilhaArray<>();

        for (String token : expressao.split(" ")) {
            switch (token) {
                case "+" -> {
                    double b = pilha.desempilhar();
                    double a = pilha.desempilhar();
                    pilha.empilhar(a + b);
                }
                case "-" -> {
                    double b = pilha.desempilhar();
                    double a = pilha.desempilhar();
                    pilha.empilhar(a - b);
                }
                case "*" -> {
                    double b = pilha.desempilhar();
                    double a = pilha.desempilhar();
                    pilha.empilhar(a * b);
                }
                case "/" -> {
                    double b = pilha.desempilhar();
                    double a = pilha.desempilhar();
                    if (b == 0) throw new ArithmeticException("Divisão por zero");
                    pilha.empilhar(a / b);
                }
                case "%" -> {
                    double b = pilha.desempilhar();
                    double a = pilha.desempilhar();
                    if (b == 0) throw new ArithmeticException("Módulo por zero");
                    pilha.empilhar(a % b);
                }
                default -> pilha.empilhar(Double.parseDouble(token));
            }
        }
        return pilha.desempilhar();
    }
}
