package dev.thrako.receptaria.error.validation.annotation;

import dev.thrako.receptaria.error.validation.validator.PhotoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhotoValidator.class)
public @interface SupportedFileType {
    String message() default "Only PNG, APNG, BMP, JPEG, GIF, AVIF, TIFF, SVG or WEBP images are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
