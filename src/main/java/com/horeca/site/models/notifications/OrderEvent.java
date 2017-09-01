package com.horeca.site.models.notifications;

import com.horeca.site.models.hotel.services.AvailableServiceType;
import org.springframework.context.ApplicationEvent;

public abstract class OrderEvent extends ApplicationEvent {

    public OrderEvent(Object source) {
        super(source);
    }

    public abstract AvailableServiceType getServiceType();

    public abstract String getPin();
}
