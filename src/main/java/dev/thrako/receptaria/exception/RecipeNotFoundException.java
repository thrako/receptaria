package dev.thrako.receptaria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends NoSuchElementException {


    public RecipeNotFoundException (String s) {

        super(s);
    }
}
