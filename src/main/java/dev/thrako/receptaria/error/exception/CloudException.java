package dev.thrako.receptaria.error.exception;

import java.io.IOException;

public class CloudException extends RuntimeException {

    public CloudException (IOException e) {
        super(e.getMessage(), e.getCause());
    }
}
