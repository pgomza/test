package com.horeca.site.models.orders.bar;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class BarOrderPOST {

    @NotNull
    private Set<BarOrderItemPOST> items;

    public Set<BarOrderItemPOST> getItems() {
        return items;
    }

    public void setItems(Set<BarOrderItemPOST> items) {
        this.items = items;
    }
}
