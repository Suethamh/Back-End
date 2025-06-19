package br.edu.iftm.tspi.porm.sistema_jpa.validator;

import org.springframework.stereotype.Component;

import br.edu.iftm.tspi.porm.sistema_jpa.annotation.ProdutoExists;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.ProdutoRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ProdutoExistsValidator implements ConstraintValidator<ProdutoExists, Integer>{
    
    private final ProdutoRepository repository;

    public ProdutoExistsValidator(ProdutoRepository repository){
        this.repository = repository;
    }

    @Override
    public boolean isValid(Integer produtoID, ConstraintValidatorContext context){
        if (produtoID == null) {
            return false;
        }
        return repository.existsById(produtoID);
    }
}
