package com.horeca.site.models.hotel.services.petcare;

import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PetCare extends Translatable<PetCareTranslation> implements Viewable<PetCareView> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<PetCareItem> items;

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

    public Set<PetCareItem> getItems() {
        return items;
    }

    public void setItems(Set<PetCareItem> items) {
        this.items = items;
    }

    @Override
    public PetCareView toView(String preferredLanguage, String defaultLanguage) {
        PetCareView view = new PetCareView();
        view.setPrice(getPrice());
        view.setDescription(getTranslation(preferredLanguage, defaultLanguage).getDescription());

        Set<PetCareItemView> itemViews = new HashSet<>();
        for (PetCareItem item : getItems()) {
            itemViews.add(item.toView(preferredLanguage, defaultLanguage));
        }
        view.setItems(itemViews);

        return view;
    }
}
