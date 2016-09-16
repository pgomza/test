package com.horeca.site.models.hotel.services;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class DummyService {

    public enum Type { BREAKFAST, CARPARK }

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
