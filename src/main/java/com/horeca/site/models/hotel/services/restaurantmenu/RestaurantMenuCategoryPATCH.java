package com.horeca.site.models.hotel.services.restaurantmenu;

import org.hibernate.validator.constraints.NotEmpty;

public class RestaurantMenuCategoryPATCH {

    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
