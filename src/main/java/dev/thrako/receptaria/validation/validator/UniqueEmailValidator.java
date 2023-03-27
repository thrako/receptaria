package dev.thrako.receptaria.validation.validator;

import dev.thrako.receptaria.service.UserService;
import dev.thrako.receptaria.validation.annotation.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserService userService;

    public UniqueEmailValidator (UserService userService) {

        this.userService = userService;
    }

    @Override
    public void initialize (UniqueEmail constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid (String email, ConstraintValidatorContext context) {

        return !this.userService.existsByEmail(email);
    }
}
