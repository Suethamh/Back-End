package br.edu.iftm.tspi.porm.sistema_jpa.validator;

import org.springframework.stereotype.Component;

import br.edu.iftm.tspi.porm.sistema_jpa.annotation.PedidoExists;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.PedidoRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PedidoExistsValidator implements ConstraintValidator<PedidoExists, Integer> {
    
    private final PedidoRepository repository;

    public PedidoExistsValidator(PedidoRepository repository){
        this.repository = repository;
    }

    @Override
    public boolean isValid(Integer pedidoID, ConstraintValidatorContext context){
        if (pedidoID == null) {
            return false;
        }
        return repository.existsById(pedidoID);
    }
}
