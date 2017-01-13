package com.horeca.site.models.orders.roomservice;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class RoomServiceOrderPOST {

    @NotNull
    private String time; //TODO change the type

    @NotNull
    private Set<RoomServiceOrderItemPOST> items;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Set<RoomServiceOrderItemPOST> getItems() {
        return items;
    }

    public void setItems(Set<RoomServiceOrderItemPOST> items) {
        this.items = items;
    }
}
