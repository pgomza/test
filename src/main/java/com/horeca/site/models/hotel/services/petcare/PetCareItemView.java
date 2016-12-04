package com.horeca.site.models.hotel.services.petcare;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendar;

public class PetCareItemView {

    private Long id;

    private String name;

    private Price price;

    private PetCareCalendar calendar;

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

    public PetCareCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(PetCareCalendar calendar) {
        this.calendar = calendar;
    }
}
