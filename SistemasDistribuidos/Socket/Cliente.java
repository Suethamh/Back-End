package Socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws Exception{
        Socket conexao = new Socket("127.0.0.1", 2001);
        try(DataInputStream entrada = new DataInputStream(conexao.getInputStream());
            DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
             

            ThreadInput thread = new ThreadInput(entrada);
            new Thread(thread).start();

            while (true) {
                System.out.print("> ");
                String linha = teclado.readLine();
                saida.writeUTF(linha);
            }
        }
    }
}

class ThreadInput implements Runnable{
    private DataInputStream entrada;

    public ThreadInput(DataInputStream entrada){
        this.entrada = entrada;
    }

    @Override
    public void run(){
        while (true) {
            try {
                String linha = entrada.readUTF();
                if (linha.isEmpty()) {
                    System.out.println("Conexão encerrada!");
                    break;
                }
                System.out.println(linha);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
