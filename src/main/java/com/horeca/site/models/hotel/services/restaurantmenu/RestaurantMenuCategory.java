package com.horeca.site.models.hotel.services.restaurantmenu;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = @Index(name = "restaurant_menu_id", columnList = "restaurant_menu_id"))
public class RestaurantMenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_menu_category_id")
    @OrderColumn(name = "item_order")
    private List<RestaurantMenuItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestaurantMenuItem> getItems() {
        return items;
    }

    public void setItems(List<RestaurantMenuItem> items) {
        this.items = items;
    }
}
