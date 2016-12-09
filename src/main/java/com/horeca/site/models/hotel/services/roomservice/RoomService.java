package com.horeca.site.models.hotel.services.roomservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Price;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class RoomService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotEmpty
    private String description;

    @NotNull
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_service_id")
    private Set<RoomItem> items;

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

    public Set<RoomItem> getItems() {
        return items;
    }

    public void setItems(Set<RoomItem> items) {
        this.items = items;
    }
}
