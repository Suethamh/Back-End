package br.edu.iftm.tspi.porm.sistema_jpa.validator;

import org.springframework.stereotype.Component;

import br.edu.iftm.tspi.porm.sistema_jpa.annotation.ClienteExists;
import br.edu.iftm.tspi.porm.sistema_jpa.repository.ClienteRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ClienteExistsValidator implements ConstraintValidator<ClienteExists, String> {

    private final ClienteRepository repository;

    public ClienteExistsValidator(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String clienteID, ConstraintValidatorContext context){
        if(clienteID == null){
            return false;
        }
        return repository.existsById(clienteID);
    }
}