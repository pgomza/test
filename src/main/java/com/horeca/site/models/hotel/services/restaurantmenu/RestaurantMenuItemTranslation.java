package com.horeca.site.models.hotel.services.restaurantmenu;

import com.horeca.site.models.Translation;
import com.horeca.site.services.DeepCopyService;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;

@Entity
public class RestaurantMenuItemTranslation extends Translation<RestaurantMenuItem> {

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

    @Override
    public RestaurantMenuItem translate() {
        RestaurantMenuItem copy = DeepCopyService.copy(getTranslatable());
        copy.setName(getName());
        copy.setDescription(getDescription());
        return copy;
    }
}
