package com.horeca.site.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        this("The requested resource could not be found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}