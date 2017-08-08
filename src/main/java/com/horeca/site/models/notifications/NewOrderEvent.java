package com.horeca.site.models.notifications;

import com.horeca.site.models.hotel.services.AvailableServiceType;
import org.springframework.context.ApplicationEvent;

public class NewOrderEvent extends ApplicationEvent {

    private final AvailableServiceType serviceType;
    private final String pin;

    public NewOrderEvent(Object source, AvailableServiceType serviceType, String pin) {
        super(source);
        this.serviceType = serviceType;
        this.pin = pin;
    }

    public AvailableServiceType getServiceType() {
        return serviceType;
    }

    public String getPin() {
        return pin;
    }
}
