package dev.thrako.receptaria.common.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException (String reason) {

        super(HttpStatus.NOT_FOUND, reason);
    }
}
