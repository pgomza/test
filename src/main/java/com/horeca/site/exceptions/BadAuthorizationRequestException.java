package com.horeca.site.exceptions;

public class BadAuthorizationRequestException extends RuntimeException {

    public BadAuthorizationRequestException() {
        this("Invalid authorization request");
    }

    public BadAuthorizationRequestException(String message) {
        super(message);
    }
}
