package com.horeca.site.models.orders.breakfast;

import com.horeca.site.models.orders.OrderStatus;

import java.util.Set;

public class BreakfastOrderView {

    private Long id;

    private OrderStatus status;

    private Set<BreakfastOrderEntryView> entries;

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

    public Set<BreakfastOrderEntryView> getEntries() {
        return entries;
    }

    public void setEntries(Set<BreakfastOrderEntryView> entries) {
        this.entries = entries;
    }
}
