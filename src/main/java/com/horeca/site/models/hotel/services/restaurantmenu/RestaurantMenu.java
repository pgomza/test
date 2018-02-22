package com.horeca.site.models.hotel.services.restaurantmenu;

import com.horeca.site.models.hotel.services.StandardServiceModel;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

@Entity
@Audited
public class RestaurantMenu extends StandardServiceModel<RestaurantMenuCategory> {
}
