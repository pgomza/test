package com.horeca.site.models.hotel.services.petcare;

import com.horeca.site.models.Price;

import java.util.Set;

public class PetCareView {

    private Price price;

    private String description;

    private Set<PetCareItemView> items;

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

    public Set<PetCareItemView> getItems() {
        return items;
    }

    public void setItems(Set<PetCareItemView> items) {
        this.items = items;
    }
}
