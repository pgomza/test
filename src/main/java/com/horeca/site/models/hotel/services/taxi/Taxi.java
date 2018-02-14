package com.horeca.site.models.hotel.services.taxi;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.HotelServiceModel;
import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Audited
public class Taxi implements HotelServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Price price;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "taxi_id")
    private Set<TaxiItem> items;

    @NotNull
    private Boolean available;

    @Override
    public Boolean getAvailable() {
        return available;
    }

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

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
