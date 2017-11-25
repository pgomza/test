package com.horeca.site.models.orders.petcare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class PetCareOrder extends Order {

    @Translatable
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PetCareItem item;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    public PetCareItem getItem() {
        return item;
    }

    public void setItem(PetCareItem item) {
        this.item = item;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
