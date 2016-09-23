package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.CustomDateSerializer;
import com.horeca.site.models.Price;

import java.util.Date;
import java.util.Set;

public class BreakfastView {

    private String description;

    private Price price;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date fromHour;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date toHour;

    private Set<BreakfastCategoryView> menu;

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

    public Date getFromHour() {
        return fromHour;
    }

    public void setFromHour(Date fromHour) {
        this.fromHour = fromHour;
    }

    public Date getToHour() {
        return toHour;
    }

    public void setToHour(Date toHour) {
        this.toHour = toHour;
    }

    public Set<BreakfastCategoryView> getMenu() {
        return menu;
    }

    public void setMenu(Set<BreakfastCategoryView> menu) {
        this.menu = menu;
    }
}
