package iftm.mth.isolamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import iftm.mth.isolamento.model.Produto;

@EnableJpaRepositories
public interface EstoqueRepository extends JpaRepository<Produto, Integer> {

    @Modifying
    @Query("update Produto p set p.estoque = p.estoque - :quantidade where p.id = :id")
    int atualizaEstoque(@Param("id") Integer id, @Param("quantidade") int quantidade);

}
