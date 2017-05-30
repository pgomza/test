package com.horeca.site.models.orders.bar;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class BarOrderPOST {

    @NotNull
    private Set<BarOrderItemPOST> items;

    @NotEmpty
    private String tableNumber;

    public Set<BarOrderItemPOST> getItems() {
        return items;
    }

    public void setItems(Set<BarOrderItemPOST> items) {
        this.items = items;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
}
