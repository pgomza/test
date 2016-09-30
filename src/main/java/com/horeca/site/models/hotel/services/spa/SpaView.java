package com.horeca.site.models.hotel.services.spa;

import com.horeca.site.models.Price;

import java.util.Set;

public class SpaView {

    private Price price;

    private String description;

    private Set<SpaItemView> items;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SpaItemView> getItems() {
        return items;
    }

    public void setItems(Set<SpaItemView> items) {
        this.items = items;
    }
}
