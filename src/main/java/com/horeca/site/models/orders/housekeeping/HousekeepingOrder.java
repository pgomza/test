package com.horeca.site.models.orders.housekeeping;

import com.horeca.site.models.hotel.services.housekeeping.HousekeepingItem;
import com.horeca.site.models.orders.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class HousekeepingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    private String message;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<HousekeepingItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

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
