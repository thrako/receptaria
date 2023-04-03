package dev.thrako.receptaria.common.error.validation.annotation;

import dev.thrako.receptaria.common.error.validation.validator.UniqueRecipeForUserValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueRecipeForUserValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueRecipeForUser {

    String message () default "You already have recipe with the same title.";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}