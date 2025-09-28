package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor implements Observador{

    private List<ServidorSocketThread> conexoes = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        Servidor servidor = new Servidor();
        try(ServerSocket serverSocket = new ServerSocket(2001, 1000)){
            System.out.println("Servidor iniciado. Aguardando conexões...");
            while (true) {
                Socket conexao = serverSocket.accept();
                System.out.println("Conexão estabelecida!");
                ServidorSocketThread thread = new ServidorSocketThread(conexao,servidor);
                servidor.conexoes.add(thread);
                new Thread(thread).start();
            }
        }
    }
    @Override
    public void enviaMensagem(String mensagem) throws IOException{
        for (ServidorSocketThread thread: conexoes){
            thread.getSaida().writeUTF(mensagem);
        }
    }
}