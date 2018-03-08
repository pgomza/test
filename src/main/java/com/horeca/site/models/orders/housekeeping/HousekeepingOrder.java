package com.horeca.site.models.orders.housekeeping;

import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Audited
public class HousekeepingOrder extends Order {

    private String message;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "housekeeping_order_id")
    private Set<HousekeepingItemData> items;

    HousekeepingOrder() {}

    public HousekeepingOrder(String message, Set<HousekeepingItemData> items) {
        this.message = message;
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<HousekeepingItemData> getItems() {
        return items;
    }

    public void setItems(Set<HousekeepingItemData> items) {
        this.items = items;
    }
}
