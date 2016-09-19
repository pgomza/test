package com.horeca.site.models.hotel.services.carpark;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Price;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class CarPark extends Translatable<CarParkTranslation> implements Viewable<CarParkView> {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    @Embedded
    private Price price;

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

    @Override
    public CarParkView toView(String preferredLanguage, String defaultLanguage) {
        CarParkTranslation translation = getTranslation(preferredLanguage, defaultLanguage);
        CarParkView view = new CarParkView();
        view.setPrice(getPrice());
        view.setDescription(translation.getDescription());

        return view;
    }
}
