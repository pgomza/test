package com.horeca.site.models.cubilis;

public class CubilisConnectionStatusView {

    private CubilisConnectionStatus.Status status;
    private String message;

    CubilisConnectionStatusView() {
    }

    public CubilisConnectionStatusView(CubilisConnectionStatus.Status status) {
        this.status = status;
        this.message = status.toString();
    }

    public CubilisConnectionStatus.Status getStatus() {
        return status;
    }

    public void setStatus(CubilisConnectionStatus.Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
