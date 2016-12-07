package com.horeca.site.models.hotel.services.roomservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class RoomService extends Translatable<RoomServiceTranslation> implements Viewable<RoomServiceView> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<RoomItem> items;

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

    public Set<RoomItem> getItems() {
        return items;
    }

    public void setItems(Set<RoomItem> items) {
        this.items = items;
    }

    @Override
    public RoomServiceView toView(String preferredLanguage, String defaultLanguage) {
        RoomServiceTranslation translation = getTranslation(preferredLanguage, defaultLanguage);
        RoomServiceView view = new RoomServiceView();
        view.setPrice(getPrice());
        view.setDescription(translation.getDescription());

        List<RoomItemView> itemViews = new ArrayList<>();
        for (RoomItem item : items)
            itemViews.add(item.toView(preferredLanguage, defaultLanguage));

        view.setItems(itemViews);

        return view;
    }
}
