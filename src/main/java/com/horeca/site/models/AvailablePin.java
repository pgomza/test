package com.horeca.site.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "pin" }))
public class AvailablePin {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String pin;

    public AvailablePin() {
    }

    public AvailablePin(String pin) {
        this.pin = pin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
