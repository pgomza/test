package com.horeca.site.models.orders.rental;

import javax.validation.constraints.NotNull;

public class RentalOrderItemPOST {

    @NotNull
    private Long itemId;

    @NotNull
    private Integer count;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
