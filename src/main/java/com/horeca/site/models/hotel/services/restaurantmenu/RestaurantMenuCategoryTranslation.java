package com.horeca.site.models.hotel.services.restaurantmenu;

import com.horeca.site.models.Translation;
import com.horeca.site.services.DeepCopyService;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class RestaurantMenuCategoryTranslation extends Translation<RestaurantMenuCategory> {

    @NotEmpty
    private String name;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_menu_category_id")
    @OrderColumn(name = "item_order")
    private List<RestaurantMenuItemTranslation> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestaurantMenuItemTranslation> getItems() {
        return items;
    }

    public void setItems(List<RestaurantMenuItemTranslation> items) {
        this.items = items;
    }

    @Override
    public RestaurantMenuCategory translate() {
        RestaurantMenuCategory copy = DeepCopyService.copy(getTranslatable());
        List<RestaurantMenuItem> translatedItems = getItems().stream()
                .map(i -> i.translate())
                .collect(Collectors.toList());
        copy.setItems(translatedItems);
        return copy;
    }
}
