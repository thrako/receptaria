package dev.thrako.receptaria.web.controller.advice;

import dev.thrako.receptaria.common.message.ErrorMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ValidationErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public final ErrorMap handleBindException (BindException ex) {

        ErrorMap errorMap = new ErrorMap();
        final BindingResult bindingResult = ex.getBindingResult();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (Objects.requireNonNull(fieldError.getCode()).contains("typeMismatch")) {
                errorMap.addError("fileData", "Please select file!");
            } else {
                errorMap.addError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        for (ObjectError globalError : bindingResult.getGlobalErrors()) {
            errorMap.addError("global", globalError.getDefaultMessage());
        }

        return errorMap;
    }

}
