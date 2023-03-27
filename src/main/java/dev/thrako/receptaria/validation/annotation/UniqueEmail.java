package dev.thrako.receptaria.validation.annotation;

import dev.thrako.receptaria.validation.validator.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message () default "Email must be unique!";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}
