package com.horeca.site.models.updates;

import org.springframework.context.ApplicationEvent;

public class ChangeInHotelEvent extends ApplicationEvent {

    private final Long hotelId;

    public ChangeInHotelEvent(Object source, Long hotelId) {
        super(source);
        this.hotelId = hotelId;
    }

    public Long getHotelId() {
        return hotelId;
    }
}
