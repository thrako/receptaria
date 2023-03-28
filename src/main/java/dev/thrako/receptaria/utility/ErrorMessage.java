package dev.thrako.receptaria.utility;

import lombok.Getter;

@Getter
public class ErrorMessage {

    private final String message;

    private ErrorMessage (String message) {

        this.message = message;
    }

    public static ErrorMessage from (String message) {

        return new ErrorMessage(message);
    }

}
