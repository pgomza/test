package com.horeca.site.models.hotel.services.restaurantmenu;

import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = @Index(name = "restaurant_menu_id", columnList = "restaurant_menu_id"))
@Audited
public class RestaurantMenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    @NotEmpty
    private String name;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
