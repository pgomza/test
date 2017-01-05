package com.horeca.site.models.hotel.services.housekeeping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Price;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Housekeeping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String description;

    @NotNull
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "housekeeping_id")
    private Set<HousekeepingItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Set<HousekeepingItem> getItems() {
        return items;
    }

    public void setItems(Set<HousekeepingItem> items) {
        this.items = items;
    }
}
