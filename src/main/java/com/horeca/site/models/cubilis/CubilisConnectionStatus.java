package com.horeca.site.models.cubilis;

public class CubilisConnectionStatus {

    public enum Status {
        DISABLED("Integration with Cubilis has not been enabled"),
        SUCCESS("Connection with Cubilis has been successfuly established"),
        AUTHENTICATION_ERROR("The login and/or password is incorrect. Your login will be blocked after 5 failed attempts"),
        UNKNOWN_ERROR("An unknown error has occurred");

        private final String text;

        Status(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private Status status;
    private String message;

    public CubilisConnectionStatus(Status status) {
        this.status = status;
        this.message = status.toString();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
