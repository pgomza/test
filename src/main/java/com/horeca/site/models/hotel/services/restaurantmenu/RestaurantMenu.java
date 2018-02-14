package com.horeca.site.models.hotel.services.restaurantmenu;

import com.horeca.site.models.hotel.services.HotelServiceModel;
import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
public class RestaurantMenu implements HotelServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    private String description;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_menu_id")
    @OrderColumn(name = "category_order")
    private List<RestaurantMenuCategory> categories = new ArrayList<>();

    @NotNull
    private Boolean available;

    @Override
    public Boolean getAvailable() {
        return available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RestaurantMenuCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<RestaurantMenuCategory> categories) {
        this.categories = categories;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
