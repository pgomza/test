package com.horeca.site.models.hotel.services.spa;

import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Spa extends Translatable<SpaTranslation> implements Viewable<SpaView> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<SpaItem> items;

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

    public Set<SpaItem> getItems() {
        return items;
    }

    public void setItems(Set<SpaItem> items) {
        this.items = items;
    }

    @Override
    public SpaView toView(String preferredLanguage, String defaultLanguage) {
        SpaView view = new SpaView();
        view.setPrice(getPrice());
        view.setDescription(getTranslation(preferredLanguage, defaultLanguage).getDescription());

        Set<SpaItemView> itemViews = new HashSet<>();
        for (SpaItem item : getItems()) {
            itemViews.add(item.toView(preferredLanguage, defaultLanguage));
        }
        view.setItems(itemViews);

        return view;
    }
}
