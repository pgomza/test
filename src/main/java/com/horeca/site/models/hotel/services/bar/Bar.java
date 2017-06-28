package com.horeca.site.models.hotel.services.bar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import org.joda.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Bar {

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "bar_id")
    @OrderColumn(name = "category_order")
    private List<BarCategory> categories;

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

    public List<BarCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<BarCategory> categories) {
        this.categories = categories;
    }
}
