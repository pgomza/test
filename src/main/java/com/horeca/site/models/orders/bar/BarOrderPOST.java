package com.horeca.site.models.orders.bar;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class BarOrderPOST {

    @NotNull
    private String time; //TODO change the type

    @NotNull
    private Set<BarOrderItemPOST> items;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Set<BarOrderItemPOST> getItems() {
        return items;
    }

    public void setItems(Set<BarOrderItemPOST> items) {
        this.items = items;
    }
}
