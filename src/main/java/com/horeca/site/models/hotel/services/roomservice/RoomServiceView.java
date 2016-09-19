package com.horeca.site.models.hotel.services.roomservice;

import com.horeca.site.models.Price;

import java.util.List;

public class RoomServiceView {

    private Price price;

    private String description;

    private List<RoomItemView> items;

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

    public List<RoomItemView> getItems() {
        return items;
    }

    public void setItems(List<RoomItemView> items) {
        this.items = items;
    }
}
