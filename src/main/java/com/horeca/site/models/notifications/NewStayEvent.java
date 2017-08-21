package com.horeca.site.models.notifications;

import com.horeca.site.models.stay.Stay;
import org.springframework.context.ApplicationEvent;

public class NewStayEvent extends ApplicationEvent {

    private final Stay stay;

    public NewStayEvent(Object source, Stay stay) {
        super(source);
        this.stay = stay;
    }

    public Stay getStay() {
        return stay;
    }
}
