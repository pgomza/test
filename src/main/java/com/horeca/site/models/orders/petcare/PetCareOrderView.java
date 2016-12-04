package com.horeca.site.models.orders.petcare;

import com.horeca.site.models.hotel.services.petcare.PetCareItemView;
import com.horeca.site.models.orders.OrderStatus;

public class PetCareOrderView {

    private Long id;

    private OrderStatus status;

    private PetCareItemView item;

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

    public PetCareItemView getItem() {
        return item;
    }

    public void setItem(PetCareItemView item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
