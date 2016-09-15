package com.horeca.site.exceptions;

public class BusinessRuleViolationException extends RuntimeException {

    public BusinessRuleViolationException() {
        this("The request violates at least one of the business rules");
    }

    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
