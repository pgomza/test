package com.horeca.site.models.hotel.services.restaurantmenu;

import com.horeca.site.models.Translation;
import com.horeca.site.services.DeepCopyService;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class RestaurantMenuTranslation extends Translation<RestaurantMenu> {

    @NotEmpty
    private String description;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_menu_id")
    @OrderColumn(name = "category_order")
    private List<RestaurantMenuCategoryTranslation> categories;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RestaurantMenuCategoryTranslation> getCategories() {
        return categories;
    }

    public void setCategories(List<RestaurantMenuCategoryTranslation> categories) {
        this.categories = categories;
    }

    @Override
    public RestaurantMenu translate() {
        RestaurantMenu copy = DeepCopyService.copy(getTranslatable());
        List<RestaurantMenuCategory> translatedCategories = getCategories().stream()
                .map(c -> c.translate())
                .collect(Collectors.toList());
        copy.setCategories(translatedCategories);
        return copy;
    }
}
