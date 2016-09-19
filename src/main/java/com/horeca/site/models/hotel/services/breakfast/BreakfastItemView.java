package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Price;

public class BreakfastItemView {

    private Long id;

    private BreakfastItem.Type type;

    private String name;

    private Price price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BreakfastItem.Type getType() {
        return type;
    }

    public void setType(BreakfastItem.Type type) {
        this.type = type;
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
