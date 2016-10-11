package com.horeca.site.models.orders.spa;

import com.horeca.site.models.hotel.services.spa.SpaItemView;
import com.horeca.site.models.orders.OrderStatus;

public class SpaOrderView {

    private Long id;

    private OrderStatus status;

    private SpaItemView item;

    private String time;

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

    public SpaItemView getItem() {
        return item;
    }

    public void setItem(SpaItemView item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
