package com.horeca.site.models.orders.breakfast;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class BreakfastOrderPOST {

    @NotNull
    private String time; //TODO change the type

    @NotNull
    private Set<BreakfastOrderItemPOST> items;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Set<BreakfastOrderItemPOST> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastOrderItemPOST> items) {
        this.items = items;
    }
}
