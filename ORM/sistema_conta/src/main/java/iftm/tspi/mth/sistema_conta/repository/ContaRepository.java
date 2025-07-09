package iftm.tspi.mth.sistema_conta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iftm.tspi.mth.sistema_conta.domain.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>{
    
}
