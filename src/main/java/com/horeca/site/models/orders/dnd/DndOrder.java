package com.horeca.site.models.orders.dnd;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class DndOrder {

    public enum Status { ENABLED, DISABLED }

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.DISABLED;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
