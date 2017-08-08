package com.horeca.site.exceptions;

public class BadAuthenticationRequestException extends RuntimeException {

    public BadAuthenticationRequestException() {
        this("Invalid authentication request");
    }

    public BadAuthenticationRequestException(String message) {
        super(message);
    }
}
