package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Price;

public class BreakfastItemUpdate {

    private Long id;
    private String name;
    private Price price;
    private BreakfastCategory.Category type;

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

    public BreakfastCategory.Category getType() {
        return type;
    }

    public void setType(BreakfastCategory.Category type) {
        this.type = type;
    }
}
