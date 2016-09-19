package com.horeca.site.models.hotel.services.breakfast;

import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class BreakfastItem extends Translatable<BreakfastItemTranslation> implements Viewable<BreakfastItemView> {

    public enum Type { DISH, DRINK }

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull
    @Embedded
    private Price price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public BreakfastItemView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastItemTranslation translation = getTranslation(preferredLanguage, defaultLanguage);
        BreakfastItemView view = new BreakfastItemView();
        view.setId(translation.getId());
        view.setType(getType());
        view.setPrice(getPrice());
        view.setName(translation.getName());

        return view;
    }
}
