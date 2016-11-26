package com.horeca.site.models.hotel.services.petcare;

import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.petcare.calendar.PetCareCalendar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class PetCareItem extends Translatable<PetCareItemTranslation>
        implements Viewable<PetCareItemView> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Price price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private PetCareCalendar calendar;

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

    public PetCareCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(PetCareCalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public PetCareItemView toView(String preferredLanguage, String defaultLanguage) {
        PetCareItemView view = new PetCareItemView();
        view.setId(getId());
        view.setName(getTranslation(preferredLanguage, defaultLanguage).getName());
        view.setPrice(getPrice());
        return view;
    }
}
