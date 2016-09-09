package com.horeca.site.handlers;

import com.horeca.site.exceptions.NoSuchEntityException;
import com.horeca.site.validation.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class DataAccessExceptionHandler {

    @ExceptionHandler({
            NoSuchEntityException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(new Date());

        return new ResponseEntity<ErrorResponse>(errorResponse, null, HttpStatus.BAD_REQUEST);
    }
}