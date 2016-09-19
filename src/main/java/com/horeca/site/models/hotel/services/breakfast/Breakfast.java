package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Breakfast extends Translatable<BreakfastTranslation> implements Viewable<BreakfastView> {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    @Embedded
    private Price price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<BreakfastItem> items;

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

    public Set<BreakfastItem> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastItem> items) {
        this.items = items;
    }

    @Override
    public BreakfastView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastTranslation translation = getTranslation(preferredLanguage, defaultLanguage);
        BreakfastView view = new BreakfastView();
        view.setPrice(getPrice());
        view.setDescription(translation.getDescription());

        List<BreakfastItemView> itemViews = new ArrayList<>();
        for (BreakfastItem item : items) {
            BreakfastItemView itemView = item.toView(preferredLanguage, defaultLanguage);
            itemViews.add(itemView);
        }
        view.setItems(itemViews);

        return view;
    }
}
