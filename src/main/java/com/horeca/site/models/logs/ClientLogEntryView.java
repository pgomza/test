package com.horeca.site.models.logs;

import com.horeca.site.models.logs.ClientLogEntry.Level;

public class ClientLogEntryView {

    private Level level;
    private String message;
    private String createdAt;

    ClientLogEntryView() {}

    public ClientLogEntryView(Level level, String message, String createdAt) {
        this.level = level;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
