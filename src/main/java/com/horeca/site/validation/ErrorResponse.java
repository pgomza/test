package com.horeca.site.validation;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorResponse {

    private String message;
    private Integer status;
    private Date timestamp;
    private Map<String, List<ValidationError>> validationErrors = new HashMap<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, List<ValidationError>> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, List<ValidationError>> validationErrors) {
        this.validationErrors = validationErrors;
    }
}