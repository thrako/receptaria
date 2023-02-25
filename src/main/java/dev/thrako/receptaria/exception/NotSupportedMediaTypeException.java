package dev.thrako.receptaria.exception;

public class NotSupportedMediaTypeException extends RuntimeException {

    public NotSupportedMediaTypeException () {
        super();
    }

    public NotSupportedMediaTypeException (String message) {

        super(message);
    }

    public NotSupportedMediaTypeException (String message, Throwable cause) {

        super(message, cause);
    }
}
