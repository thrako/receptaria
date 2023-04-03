package dev.thrako.receptaria.common.error.validation.validator;

import dev.thrako.receptaria.common.error.validation.annotation.UniqueEmail;
import dev.thrako.receptaria.service.UserService;
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
