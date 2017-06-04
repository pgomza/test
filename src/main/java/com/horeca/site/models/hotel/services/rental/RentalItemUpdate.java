package com.horeca.site.models.hotel.services.rental;

import com.horeca.site.models.Price;

public class RentalItemUpdate {

    private Long id;
    private String name;
    private Price price;
    private boolean available;
    private RentalCategory.Category type;

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public RentalCategory.Category getType() {
        return type;
    }

    public void setType(RentalCategory.Category type) {
        this.type = type;
    }
}
