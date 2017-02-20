package com.horeca.site.models.hotel.services.taxi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Price;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Taxi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "taxi_id")
    private Set<TaxiItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Set<TaxiItem> getItems() {
        return items;
    }

    public void setItems(Set<TaxiItem> items) {
        this.items = items;
    }
}
