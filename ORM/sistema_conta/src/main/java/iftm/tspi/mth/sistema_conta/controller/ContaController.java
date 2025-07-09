package iftm.tspi.mth.sistema_conta.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iftm.tspi.mth.sistema_conta.dto.TransferenciaDto;
import iftm.tspi.mth.sistema_conta.service.ContaService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/conta")
public class ContaController {
    
    private final ContaService service;

    public ContaController(ContaService service){
        this.service = service;
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferir(@Valid @RequestBody TransferenciaDto dto) {
        service.transferencia(dto);
        return ResponseEntity.ok("Transferencia ok");
    }
    
}
