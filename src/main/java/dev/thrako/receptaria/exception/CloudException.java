package dev.thrako.receptaria.exception;

import java.io.IOException;

public class CloudException extends RuntimeException {

    public CloudException (IOException e) {
        super(e.getMessage(), e.getCause());
    }
}
