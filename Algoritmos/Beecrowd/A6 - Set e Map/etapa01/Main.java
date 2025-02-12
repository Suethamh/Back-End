import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);

        HashSet<Aluno> alunos = new HashSet<>();
       

        while(true){

            String matricula = ler.nextLine();
            
            if (matricula.equals("0")) {
                break;
            }

            Aluno aluno = new Aluno(matricula, ler.nextLine());
            alunos.add(aluno);

        }

        System.out.println("Alunos:");
        for (Aluno x : alunos) {
            System.out.println(x.getNome());
        }
    }
}

