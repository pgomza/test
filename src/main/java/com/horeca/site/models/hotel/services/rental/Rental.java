package com.horeca.site.models.hotel.services.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import org.joda.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @NotNull
    @Embedded
    private Price price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime fromHour;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime toHour;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rental_id")
    private Set<RentalCategory> categories;

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

    public LocalTime getFromHour() {
        return fromHour;
    }

    public void setFromHour(LocalTime fromHour) {
        this.fromHour = fromHour;
    }

    public LocalTime getToHour() {
        return toHour;
    }

    public void setToHour(LocalTime toHour) {
        this.toHour = toHour;
    }

    public Set<RentalCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<RentalCategory> categories) {
        this.categories = categories;
    }
}
