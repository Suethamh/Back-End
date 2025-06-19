package br.edu.iftm.tspi.porm.sistema_jpa.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.edu.iftm.tspi.porm.sistema_jpa.validator.ClienteExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ClienteExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClienteExists {

    String message() default "Cliente não encontrado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};    
}
