package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.*;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Breakfast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String description;

    @NotNull
    @Embedded
    private Price price;

    private Date fromHour;

    private Date toHour;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "breakfast_id")
    private Set<BreakfastCategory> categories;

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

    public Date getFromHour() {
        return fromHour;
    }

    public void setFromHour(Date fromHour) {
        this.fromHour = fromHour;
    }

    public Date getToHour() {
        return toHour;
    }

    public void setToHour(Date toHour) {
        this.toHour = toHour;
    }

    public Set<BreakfastCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<BreakfastCategory> categories) {
        this.categories = categories;
    }
}
