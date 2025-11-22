package iftm.mth.isolamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import iftm.mth.isolamento.model.ContaPessimista;
import jakarta.persistence.LockModeType;

public interface ContaPessimistaRepository extends JpaRepository<ContaPessimista, String>{
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ContaPessimista c where c.numero = :numero")
    ContaPessimista findByNumeroWithLock(@Param("numero") String numero);
}
