package iftm.mth.isolamento.service;

import org.springframework.stereotype.Service;

import iftm.mth.isolamento.model.ContaPessimista;
import iftm.mth.isolamento.repository.ContaPessimistaRepository;
import jakarta.transaction.Transactional;

@Service
public class ContaPessimistaService {
    
    private final ContaPessimistaRepository contaPessimistaRepository;
    
    public ContaPessimistaService(ContaPessimistaRepository contaPessimistaRepository){
        this.contaPessimistaRepository = contaPessimistaRepository;
    }

    @Transactional
    public void deposita(String numero, Double valor){
        ContaPessimista conta = contaPessimistaRepository.findByNumeroWithLock(numero);

        if(conta == null){
            throw new IllegalArgumentException("Conta não encontrada");
        }
        conta.setSaldo(conta.getSaldo() + valor);

        contaPessimistaRepository.save(conta);
    }

    @Transactional
    public void saque(String numero, Double valor){
        ContaPessimista conta = contaPessimistaRepository.findByNumeroWithLock(numero);

        if(conta == null){
            throw new IllegalArgumentException("Conta não encontrada");
        }

        if(conta.getSaldo() < valor){
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        try{
            Thread.sleep(10000);
        }catch(Exception e){

        }

        conta.setSaldo(conta.getSaldo() - valor);
        contaPessimistaRepository.save(conta);
    }
}
