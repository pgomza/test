package com.horeca.site.models.orders.dnd;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class DndOrderUPDATE {

    @NotNull
    @Enumerated(EnumType.STRING)
    private DndOrder.Status status;

    public DndOrder.Status getStatus() {
        return status;
    }

    public void setStatus(DndOrder.Status status) {
        this.status = status;
    }
}
