package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.services.StandardServiceModel;
import org.hibernate.envers.Audited;
import org.joda.time.LocalTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Audited
public class Breakfast extends StandardServiceModel<BreakfastCategory> {

    @NotNull
    @Embedded
    private Price price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime fromHour;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime toHour;

    Breakfast() {}

    public Breakfast(String description, List<BreakfastCategory> categories, Price price, LocalTime fromHour, LocalTime toHour) {
        super(description, categories);
        this.price = price;
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public LocalTime getFromHour() {
        return fromHour;
    }

    public void setFromHour(LocalTime fromHour) {
        this.fromHour = fromHour;
    }

    public LocalTime getToHour() {
        return toHour;
    }

    public void setToHour(LocalTime toHour) {
        this.toHour = toHour;
    }
}
