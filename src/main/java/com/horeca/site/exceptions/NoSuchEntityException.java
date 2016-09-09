package com.horeca.site.exceptions;

import org.springframework.dao.DataAccessException;

public class NoSuchEntityException extends DataAccessException {

    public NoSuchEntityException(String message) {
        super(message);
    }
}