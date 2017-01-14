package com.horeca.site.models.orders;

public interface Order {

    Long getId();
    void setId(Long id);

    OrderStatus getStatus();
    void setStatus(OrderStatus status);
}
