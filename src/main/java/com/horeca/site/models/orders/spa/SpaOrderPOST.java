package com.horeca.site.models.orders.spa;

import javax.validation.constraints.NotNull;

public class SpaOrderPOST {

    @NotNull
    private Long itemId;

    @NotNull
    private String time;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
