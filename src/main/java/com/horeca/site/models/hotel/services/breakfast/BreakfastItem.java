package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class BreakfastItem extends Translatable<BreakfastItemTranslation>
        implements Viewable<BreakfastItemView> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Price price;

    @NotNull
    private boolean available = true;

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public BreakfastItemView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastItemView view = new BreakfastItemView();
        view.setId(getId());
        view.setPrice(getPrice());
        view.setName(getTranslation(preferredLanguage, defaultLanguage).getName());
        view.setAvailable(isAvailable());

        return view;
    }
}
