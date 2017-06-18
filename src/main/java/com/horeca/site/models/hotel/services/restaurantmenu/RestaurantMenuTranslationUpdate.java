package com.horeca.site.models.hotel.services.restaurantmenu;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuTranslationUpdate {

    @NotEmpty
    private String description;
    private List<RestaurantMenuCategoryTranslationUpdate> categories = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RestaurantMenuCategoryTranslationUpdate> getCategories() {
        return categories;
    }

    public void setCategories(List<RestaurantMenuCategoryTranslationUpdate> categories) {
        this.categories = categories;
    }
}
