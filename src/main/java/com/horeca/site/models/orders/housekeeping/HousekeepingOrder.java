package com.horeca.site.models.orders.housekeeping;

import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Audited
public class HousekeepingOrder extends Order {

    private String message;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="housekeeping_order_housekeeping_item",
            joinColumns=@JoinColumn(name="housekeeping_order_id"),
            inverseJoinColumns=@JoinColumn(name="housekeeping_item_id"))
    private Set<HousekeepingItem> items;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<HousekeepingItem> getItems() {
        return items;
    }

    public void setItems(Set<HousekeepingItem> items) {
        this.items = items;
    }
}
