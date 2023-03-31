package dev.thrako.receptaria.error.validation.validator;

import dev.thrako.receptaria.error.validation.annotation.PasswordsMatch;
import dev.thrako.receptaria.model.entity.user.dto.UserRegistrationBM;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationBM> {

    @Override
    public void initialize (PasswordsMatch constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid (UserRegistrationBM candidate, ConstraintValidatorContext context) {

        final String password = candidate.getPassword();
        final String confirmPassword = candidate.getConfirmPassword();

        return password.equals(confirmPassword);
    }
}
