package dev.thrako.receptaria.common.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAuthorizedException extends ResponseStatusException {

    public NotAuthorizedException (String reason) {

        super(HttpStatus.FORBIDDEN, reason);
    }
}
