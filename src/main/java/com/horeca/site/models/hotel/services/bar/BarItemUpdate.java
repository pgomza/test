package com.horeca.site.models.hotel.services.bar;

import com.horeca.site.models.Price;

import javax.validation.constraints.NotNull;

public class BarItemUpdate {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Price price;
    private boolean available;
    @NotNull
    private BarCategory.Category type;

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

    public BarCategory.Category getType() {
        return type;
    }

    public void setType(BarCategory.Category type) {
        this.type = type;
    }
}
