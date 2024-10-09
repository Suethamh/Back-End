import java.util.Scanner;

public class Main {

    static int n = 3;
    static int[][] mapa = new int[n][n];
    static char[][] mapaXO = new char[n][n];

    public static void preencherMapa(int[][] mapa, char[][] mapaXO){
        int cont = 1;
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                mapa[i][j] = cont;
                mapaXO[i][j] = '~';
                cont++;
            }
        }
    }

    public static void mostrarMapa(int[][] mapa, char[][] mapaXO){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                System.out.print(mapaXO[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();

        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                System.out.print(mapa[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int display(int[][] mapa, Scanner ler, char jogador){
        int posicao = 0;

        System.out.println("Tabuleiro atual:");
        System.out.println();

        mostrarMapa(mapa, mapaXO);
        System.out.println();

        System.out.printf("Jogador'%s'é a sua vez!\n", jogador);

        System.out.printf("Escolha uma posição: ");
        posicao = ler.nextInt();

        while(posicao < 1 || posicao > 9) {
            restricao();

            System.out.printf("Escolha uma posição: ");    
            posicao = ler.nextInt();
        }

        return posicao;
    }

    public static boolean validacao(int posicao, char[][] mapaXO){
        int row = (posicao - 1) / n;
        int col = (posicao -1) % n;
        return mapaXO[row][col] == '~';
    }

    public static void atualizarMapaXO(int posicao, char[][] mapaXO, char jogador){
        int row = (posicao - 1) / n;
        int col = (posicao - 1) % n;
        mapaXO[row][col] = jogador;
    }

    public static void restricao(){
        System.out.println();
        System.out.println("A posição desejada já está ocupada ou não existe.");
    }

    public static boolean verificarVitoria(char jogador, char[][] mapaXO){
        for (int i = 0; i < n; i++) {
            if (mapaXO[i][0] == jogador && mapaXO[i][1] == jogador && mapaXO[i][2] == jogador) return true;
            if (mapaXO[0][i] == jogador && mapaXO[1][i] == jogador && mapaXO[2][i] == jogador) return true;
        }
        if (mapaXO[0][0] == jogador && mapaXO[1][1] == jogador && mapaXO[2][2] == jogador) return true;
        if (mapaXO[0][2] == jogador && mapaXO[1][1] == jogador && mapaXO[2][0] == jogador) return true;
        return false;
    }

    public static boolean empate(char[][] mapaXO){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(mapaXO[i][j] == '~'){
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        char jogador = 'X';
        boolean flag = false;
        int posicao;
        boolean valido;
        boolean velha;
        
        preencherMapa(mapa, mapaXO);
        boolean resultado = false;

        do {
            flag = !flag;
            jogador = flag ? 'X' : 'O';

            do{
                posicao = display(mapa, ler, jogador);
                valido = validacao(posicao, mapaXO);
                if (valido == false) {
                    restricao();
                }
            }while(valido == false);

            atualizarMapaXO(posicao, mapaXO, jogador);
            resultado = verificarVitoria(jogador, mapaXO);

            if(resultado == true) {
                System.out.printf("jogador %s venceu!!\n", jogador);
                System.out.println();
                mostrarMapa(mapa, mapaXO);
            }else{
                velha = empate(mapaXO);
                if(velha == true) {
                    System.out.println("Deu velha!!");
                    resultado = true;
                }
            }

        }while(resultado == false);
        
        ler.close();
    }
}