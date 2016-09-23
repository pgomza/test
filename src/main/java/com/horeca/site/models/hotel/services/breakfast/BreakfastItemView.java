package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Price;

public class BreakfastItemView {

    private Long id;

    private String name;

    private Price price;

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
}
