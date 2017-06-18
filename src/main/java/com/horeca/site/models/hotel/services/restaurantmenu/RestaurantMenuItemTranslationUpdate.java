package com.horeca.site.models.hotel.services.restaurantmenu;

import org.hibernate.validator.constraints.NotEmpty;

public class RestaurantMenuItemTranslationUpdate {

    @NotEmpty
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
