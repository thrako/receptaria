package dev.thrako.receptaria.common.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalTargetException extends ResponseStatusException {


    public IllegalTargetException (String reason) {

        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
