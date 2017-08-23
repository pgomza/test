package com.horeca.site.models.hotel.services.restaurantmenu;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
public class RestaurantMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_menu_id")
    @OrderColumn(name = "category_order")
    private List<RestaurantMenuCategory> categories = new ArrayList<>();

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
}
