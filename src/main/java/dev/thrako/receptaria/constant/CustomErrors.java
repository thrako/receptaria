package dev.thrako.receptaria.constant;

import dev.thrako.receptaria.error.message.ErrorMessage;

public enum CustomErrors {
    NOT_SPECIFIED ("Ooops, something bad happened. Do not worry, an error message has been logged." +
            " Our engineers will look at it and see what they can do. Sometimes they even manage to fix a bug." +
            " Do not lose hope, not all is lost."),
    BAD_REQUEST ("Error 400: Sorry, it seems your browser is not responding properly or may be you accidentally made a typo in the url."),
    FORBIDDEN ("Error 403: Sorry! You are not authorized to access this resource."),
    NOT_FOUND ("Error 404: The page you are looking for was not found. You may have mistyped the address or the page may have been moved if ever existed."),
    INTERNAL_SERVER_ERROR ("Error 500: Something bad happened at the server side. Please try again later and if the problem persists contact support@receptaria.com."),
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
