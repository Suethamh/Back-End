package br.edu.iftm.tspi.porm.sistema_jpa.validator;

import org.springframework.stereotype.Component;

import br.edu.iftm.tspi.porm.sistema_jpa.annotation.CategoriaExists;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.CategoriaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class CategoriaExistsValidator implements 
            ConstraintValidator<CategoriaExists, Integer> {

    private final CategoriaRepository repository;

    public CategoriaExistsValidator(CategoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(Integer categoriaId, 
                           ConstraintValidatorContext context) {
        if (categoriaId == null) {
            return false;
        } 
        return repository.existsById(categoriaId);
    }
    


}
