package com.horeca.site.models.orders;

import javax.validation.constraints.NotNull;

public class OrderStatusPUT {

    @NotNull
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
