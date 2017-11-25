package com.horeca.site.models.hotel.services.spa;

import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Audited
public class Spa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    private String description;

    @NotNull
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "spa_id")
    private Set<SpaItem> items;

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

    public Set<SpaItem> getItems() {
        return items;
    }

    public void setItems(Set<SpaItem> items) {
        this.items = items;
    }
}
