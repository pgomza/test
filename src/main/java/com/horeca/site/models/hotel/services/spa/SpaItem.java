package com.horeca.site.models.hotel.services.spa;

import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.spa.calendar.SpaCalendar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SpaItem extends Translatable<SpaItemTranslation>
        implements Viewable<SpaItemView> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Price price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private SpaCalendar calendar;

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

    public SpaCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(SpaCalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public SpaItemView toView(String preferredLanguage, String defaultLanguage) {
        SpaItemView view = new SpaItemView();
        view.setId(getId());
        view.setName(getTranslation(preferredLanguage, defaultLanguage).getName());
        view.setPrice(getPrice());
        return view;
    }
}
