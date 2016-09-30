package com.horeca.site.models.hotel.services.spa;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendar;

public class SpaItemView {

    private Long id;

    private String name;

    private Price price;

    private SpaCalendar calendar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public SpaCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(SpaCalendar calendar) {
        this.calendar = calendar;
    }
}
