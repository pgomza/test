package com.horeca.site.models.orders.breakfast;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class BreakfastOrderPOST {

    @NotNull
    private Set<BreakfastOrderEntryPOST> entries;

    public Set<BreakfastOrderEntryPOST> getEntries() {
        return entries;
    }

    public void setEntries(Set<BreakfastOrderEntryPOST> entries) {
        this.entries = entries;
    }
}
