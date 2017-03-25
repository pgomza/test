package com.horeca.site.exceptions;

public class UnauthorizedException extends RuntimeException {

    public static final String MESSAGE =
            "The request has not been applied because it lacks valid authentication credentials for the target resource";

    public UnauthorizedException() {
        this(MESSAGE);
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable throwable) {
        this(MESSAGE, throwable);
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
