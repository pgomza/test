package com.horeca.site.models.hotel.services.bar;

import com.horeca.site.models.Price;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Bar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @NotNull
    @Embedded
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "bar_id")
    private Set<BarCategory> categories;

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

    public Set<BarCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<BarCategory> categories) {
        this.categories = categories;
    }
}
