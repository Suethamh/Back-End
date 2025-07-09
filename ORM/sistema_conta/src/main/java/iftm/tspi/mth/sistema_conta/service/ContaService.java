package iftm.tspi.mth.sistema_conta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iftm.tspi.mth.sistema_conta.domain.Conta;
import iftm.tspi.mth.sistema_conta.dto.TransferenciaDto;
import iftm.tspi.mth.sistema_conta.repository.ContaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ContaService {
    
    private final ContaRepository repository;

    public ContaService(ContaRepository repository){
        this.repository = repository;
    }

    @Transactional
    public void transferencia(TransferenciaDto dto){
        Conta contaOrigem = findById(dto.getOrigem());
        Conta contaDestino = findById(dto.getDestino());
        Double valor = dto.getValor();
        contaOrigem.saque(valor);
        repository.save(contaOrigem);
        contaDestino.deposita(valor);
        repository.save(contaDestino);
    }

    public Conta findById(Long id){
        return repository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Conta não existe: " +id));
    }
}
