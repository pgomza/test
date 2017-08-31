package com.horeca.site.models.notifications;

import com.horeca.site.models.hotel.services.AvailableServiceType;

public class NewOrderEvent extends OrderEvent {

    private final AvailableServiceType serviceType;
    private final String pin;

    public NewOrderEvent(Object source, AvailableServiceType serviceType, String pin) {
        super(source);
        this.serviceType = serviceType;
        this.pin = pin;
    }

    @Override
    public AvailableServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public String getPin() {
        return pin;
    }
}
