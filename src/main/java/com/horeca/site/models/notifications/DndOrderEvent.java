package com.horeca.site.models.notifications;

import com.horeca.site.models.hotel.services.AvailableServiceType;

public class DndOrderEvent extends OrderEvent {

    private final String pin;

    public DndOrderEvent(Object source, String pin) {
        super(source);
        this.pin = pin;
    }

    @Override
    public AvailableServiceType getServiceType() {
        return AvailableServiceType.DND;
    }

    @Override
    public String getPin() {
        return pin;
    }
}
