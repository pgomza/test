package com.horeca.site.models.orders.spa;

import com.horeca.site.models.Price;
import com.horeca.site.models.orders.OrderStatus;

import java.util.Set;

public class SpaOrderView {

    private Long id;

    private OrderStatus status;

    private Price total;

    private Set<SpaOrderEntryView> items;

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

    public Price getTotal() {
        return total;
    }

    public void setTotal(Price total) {
        this.total = total;
    }

    public Set<SpaOrderEntryView> getItems() {
        return items;
    }

    public void setItems(Set<SpaOrderEntryView> items) {
        this.items = items;
    }
}
