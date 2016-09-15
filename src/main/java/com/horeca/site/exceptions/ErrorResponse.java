package com.horeca.site.exceptions;

import com.google.appengine.repackaged.org.joda.time.DateTime;

public class ErrorResponse {

    private String message;
    private String timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
