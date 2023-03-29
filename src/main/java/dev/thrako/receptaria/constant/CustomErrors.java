package dev.thrako.receptaria.constant;

import dev.thrako.receptaria.error.ErrorMessage;

public enum CustomErrors {
    INTERNAL_SERVER_ERROR ("Error 500: Something bad happened at the server side. Please try again later and if the problem persists contact support@receptaria.com."),
    NOT_FOUND ("Error 404: The page you are looking for was not found. You may have mistyped the address or the page may have been moved if ever existed.")
    ;

    public final String message;


    CustomErrors (String message) {

        this.message = message;
    }

    public String getMessage () {
        return this.message;
    }

    public ErrorMessage getErrorMessage () {

        return ErrorMessage.from(this.message);
    }
}
