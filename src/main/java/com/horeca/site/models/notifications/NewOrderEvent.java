package com.horeca.site.models.notifications;

import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.stay.Stay;
import org.springframework.context.ApplicationEvent;

public class NewOrderEvent extends ApplicationEvent {

    private final AvailableServiceType serviceType;
    private final Stay stay;

    public NewOrderEvent(Object source, AvailableServiceType serviceType, Stay stay) {
        super(source);
        this.serviceType = serviceType;
        this.stay = stay;
    }

    public AvailableServiceType getServiceType() {
        return serviceType;
    }

    public Stay getStay() {
        return stay;
    }
}
