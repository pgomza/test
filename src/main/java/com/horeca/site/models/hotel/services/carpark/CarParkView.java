package com.horeca.site.models.hotel.services.carpark;

import com.horeca.site.models.Price;

public class CarParkView {

    private String description;

    private Price price;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
