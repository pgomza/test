package com.horeca.site.models.orders.rental;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class RentalOrderPOST {

    @NotNull
    private Set<RentalOrderItemPOST> items;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime time;

    public Set<RentalOrderItemPOST> getItems() {
        return items;
    }

    public void setItems(Set<RentalOrderItemPOST> items) {
        this.items = items;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
