package iftm.mth.isolamento.service;

import org.springframework.stereotype.Service;

import iftm.mth.isolamento.model.ContaOtimista;
import iftm.mth.isolamento.repository.ContaOtimistaRepository;
import jakarta.transaction.Transactional;

@Service
public class ContaOtimistaService {
    
    private final ContaOtimistaRepository contaOtimistaRepository;

    public ContaOtimistaService(ContaOtimistaRepository contaOtimistaRepository){
        this.contaOtimistaRepository = contaOtimistaRepository;
    }

    @Transactional
    public void saque(String numero, Double valor){
        ContaOtimista conta = contaOtimistaRepository.findById(numero)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        
        if(conta.getSaldo() < valor){
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo() - valor);

        contaOtimistaRepository.save(conta);
            
    }

    @Transactional
    public void deposita(String numero, Double valor){
        ContaOtimista conta = contaOtimistaRepository.findById(numero)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        

        try{
            Thread.sleep(10000);
        }catch(Exception e){

        }
        conta.setSaldo(conta.getSaldo() + valor);
        contaOtimistaRepository.save(conta);
    }
}
