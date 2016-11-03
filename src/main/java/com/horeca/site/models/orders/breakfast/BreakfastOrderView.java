package com.horeca.site.models.orders.breakfast;

import com.horeca.site.models.Price;
import com.horeca.site.models.orders.OrderStatus;

import java.util.Set;

public class BreakfastOrderView {

    private Long id;

    private OrderStatus status;

    private Price total;

    private Set<BreakfastOrderItemView> entries;

    private String time; //TODO change the type

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Set<BreakfastOrderItemView> getEntries() {
        return entries;
    }

    public void setEntries(Set<BreakfastOrderItemView> entries) {
        this.entries = entries;
    }
}
