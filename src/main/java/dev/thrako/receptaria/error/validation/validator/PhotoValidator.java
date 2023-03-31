package dev.thrako.receptaria.error.validation.validator;

import dev.thrako.receptaria.error.validation.annotation.SupportedFileType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class PhotoValidator implements ConstraintValidator<SupportedFileType, MultipartFile> {

    @Override
    public void initialize (SupportedFileType constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid (MultipartFile multipartFile, ConstraintValidatorContext context) {

        if (null == multipartFile) {
            return true;
        }
        return PhotoExtensionValidator.validate(multipartFile);
    }
}
