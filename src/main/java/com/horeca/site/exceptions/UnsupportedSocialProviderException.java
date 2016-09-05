package com.horeca.site.exceptions;

public class UnsupportedSocialProviderException extends RuntimeException {

    public UnsupportedSocialProviderException(String message) {
        super(message);
    }
}
