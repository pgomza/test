package com.horeca.site.models.orders.petcare;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;

public class PetCareOrderPOST {

    @NotNull
    private Long itemId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
