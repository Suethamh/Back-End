package iftm.mth.isolamento.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import iftm.mth.isolamento.model.Produto;
import jakarta.persistence.LockModeType;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Produto p where p.id = :id")
    Produto findByIdWithLock(Long id);
}
