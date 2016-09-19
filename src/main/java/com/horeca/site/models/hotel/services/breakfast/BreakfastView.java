package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Price;

import java.math.BigDecimal;
import java.util.List;

public class BreakfastView {

    private Price price;

    private String description;

    private List<BreakfastItemView> items;

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

    public List<BreakfastItemView> getItems() {
        return items;
    }

    public void setItems(List<BreakfastItemView> items) {
        this.items = items;
    }
}
