package com.horeca.site.models.hotel.services.housekeeping;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.HotelServiceModel;
import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Audited
public class Housekeeping implements HotelServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    private String description;

    @NotNull
    private Price price;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "housekeeping_id")
    private Set<HousekeepingItem> items;

    @NotNull
    private Boolean available;

    @Override
    public Boolean getAvailable() {
        return available;
    }

    @Override
    public void setAvailable(Boolean available) {
        this.available = available;
    }

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
