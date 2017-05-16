package com.horeca.site.models.notifications;

import com.horeca.site.models.guest.Guest;
import org.springframework.context.ApplicationEvent;

public class NewStayEvent extends ApplicationEvent {

    private final Guest guest;
    private final String hotelName;
    private final String pin;

    public NewStayEvent(Object source, Guest guest, String hotelName, String pin) {
        super(source);
        this.guest = guest;
        this.hotelName = hotelName;
        this.pin = pin;
    }

    public Guest getGuest() {
        return guest;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getPin() {
        return pin;
    }
}
