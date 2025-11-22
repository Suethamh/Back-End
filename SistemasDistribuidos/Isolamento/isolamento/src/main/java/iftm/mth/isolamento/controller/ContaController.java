package iftm.mth.isolamento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import iftm.mth.isolamento.dto.ContaRequest;
import iftm.mth.isolamento.service.ContaOtimistaService;
import iftm.mth.isolamento.service.ContaPessimistaService;

@Controller
@RequestMapping("/conta")
public class ContaController {
    
    private final ContaPessimistaService contaPessimistaService;
    private final ContaOtimistaService contaOtimistaService;

    public ContaController(ContaPessimistaService contaPessimistaService,
                           ContaOtimistaService contaOtimistaService){
        this.contaPessimistaService = contaPessimistaService;
        this.contaOtimistaService = contaOtimistaService;
    }

    @PostMapping("{numero}/saqueOtimista")
    public ResponseEntity<String> saqueOtimista(@PathVariable String numero, @RequestBody ContaRequest contaRequest){
        contaOtimistaService.saque(numero, contaRequest.getValor());
        return ResponseEntity.ok("Saque Otimista realizado com sucesso");
    }

    @PostMapping("{numero}/depositaOtimista")
    public ResponseEntity<String> depositaOtimista(@PathVariable String numero, @RequestBody ContaRequest contaRequest){
        contaOtimistaService.deposita(numero, contaRequest.getValor());
        return ResponseEntity.ok("Depósito Otimista realizado com sucesso");
    }

    @PostMapping("{numero}/saquePessimista")
    public ResponseEntity<String> saquePessimista(@PathVariable String numero, @RequestBody ContaRequest contaRequest){
        contaPessimistaService.saque(numero, contaRequest.getValor());
        return ResponseEntity.ok("Saque Pessimista realizado com sucesso");
    }

    @PostMapping("{numero}/depositaPessimista")
    public ResponseEntity<String> depositaPessimista(@PathVariable String numero, @RequestBody ContaRequest contaRequest){
        contaPessimistaService.deposita(numero, contaRequest.getValor());
        return ResponseEntity.ok("Depósito Pessimista realizado com sucesso");
    }

}
