package com.horeca.site.models.updates;

import org.springframework.context.ApplicationEvent;

public class ChangeInStayEvent extends ApplicationEvent {

    private final String pin;

    public ChangeInStayEvent(Object source, String pin) {
        super(source);
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }
}
