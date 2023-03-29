package dev.thrako.receptaria.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException (String s) {

        super(s);
    }
}
