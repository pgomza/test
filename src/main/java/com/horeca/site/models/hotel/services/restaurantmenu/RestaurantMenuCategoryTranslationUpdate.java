package com.horeca.site.models.hotel.services.restaurantmenu;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuCategoryTranslationUpdate {

    @NotEmpty
    private String name;
    private List<RestaurantMenuItemTranslationUpdate> items = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestaurantMenuItemTranslationUpdate> getItems() {
        return items;
    }

    public void setItems(List<RestaurantMenuItemTranslationUpdate> items) {
        this.items = items;
    }
}
