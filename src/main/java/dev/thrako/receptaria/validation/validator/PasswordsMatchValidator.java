package dev.thrako.receptaria.validation.validator;

import dev.thrako.receptaria.model.user.dto.UserRegistrationBM;
import dev.thrako.receptaria.validation.annotation.PasswordsMatch;
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
