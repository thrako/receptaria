package dev.thrako.receptaria.common.error.exception;

import java.io.IOException;

public class CloudException extends RuntimeException {

    public CloudException (IOException e) {
        super(e.getMessage());
    }
}
