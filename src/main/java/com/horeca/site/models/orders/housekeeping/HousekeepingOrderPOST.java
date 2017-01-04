package com.horeca.site.models.orders.housekeeping;

import java.util.Set;

public class HousekeepingOrderPOST {

    private String message;
    private Set<Long> itemIds;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
