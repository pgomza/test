package com.horeca.site.models.orders.spa;

import java.util.Set;

public class SpaOrderPOST {

    private Set<SpaOrderEntryPOST> items;

    public Set<SpaOrderEntryPOST> getItems() {
        return items;
    }

    public void setItems(Set<SpaOrderEntryPOST> items) {
        this.items = items;
    }
}
