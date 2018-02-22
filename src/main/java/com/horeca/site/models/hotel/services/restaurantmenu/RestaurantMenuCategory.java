package com.horeca.site.models.hotel.services.restaurantmenu;

import com.horeca.site.models.hotel.services.StandardServiceCategoryModel;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "restaurant_menu_id", columnList = "service_id"))
@Audited
public class RestaurantMenuCategory extends StandardServiceCategoryModel<RestaurantMenuItem> {
}
