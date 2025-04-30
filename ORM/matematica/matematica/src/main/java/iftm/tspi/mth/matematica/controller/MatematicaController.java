package iftm.tspi.mth.matematica.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iftm.tspi.mth.matematica.exception.RequesicaoInvalidaException;


@RestController
@RequestMapping("/matematica")
public class MatematicaController {

    @GetMapping("/soma")
    public ResponseEntity<?> somar(
        @RequestParam(name = "a", required = true) Integer a, 
        @RequestParam(name = "b", required = true) Integer b){
        
        

        int resultado = a+b;
        return ResponseEntity.ok("resultado: " + resultado);
    }

    @GetMapping("/fatorial")
    public ResponseEntity<?> fatorar(
        @RequestParam(name = "a", required = true) Integer a){
        
        if(a < 0){
            throw new RequesicaoInvalidaException("Valor Inválido");
        }
        
        int resultado = 1;
        for(int i=a;i>0;i--){
            resultado *= i;
        }

        return ResponseEntity.ok("resultado: " + resultado);
    }

    @PostMapping("/media")
    public ResponseEntity<?> media(@RequestBody List<Integer> valores){
        if(valores.isEmpty()){
            throw new RequesicaoInvalidaException("Precisa adicionar algum valor");
        }
        int sum = 0;
        for(Integer num : valores){
            sum += num;
        }

        int resultado = sum / valores.size();
        return ResponseEntity.ok("resultado: " + resultado);
    }

    @PostMapping("/soma-linhas")
    public ResponseEntity<?> somaMatriz(@RequestBody Integer[][] matriz){
        
        if(matriz == null || matriz.length == 0){
            throw new RequesicaoInvalidaException("Matriz vazia");
        }

        int tamanhoLinha = matriz[0].length;
        for(int i=1; i<matriz.length; i++){
            if(matriz[i].length != tamanhoLinha){
                throw new RequesicaoInvalidaException("As linhas precisam ser do mesmo tamanho");
            }
        }

        int[] soma = new int[matriz.length];
        for(int i=0; i<matriz.length;i++){
            int somaLinha = 0;
            for(int j=0; j<matriz[i].length;j++){
                somaLinha += matriz[i][j];
            }
            soma[i] = somaLinha;
        }

        return ResponseEntity.ok(soma);
    }
}