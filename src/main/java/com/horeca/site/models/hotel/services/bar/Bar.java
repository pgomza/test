package com.horeca.site.models.hotel.services.bar;

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
public class Bar extends StandardServiceModel<BarCategory> {

    @NotNull
    @Embedded
    private Price price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime fromHour;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime toHour;

    Bar() {}

    public Bar(String description, List<BarCategory> categories, Boolean available, Price price, LocalTime fromHour, LocalTime toHour) {
        super(description, categories, available);
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
