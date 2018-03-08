package com.horeca.site.models.orders.petcare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class PetCareOrder extends Order {

    @Translatable
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "service_item_id")
    private PetCareItemData item;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    PetCareOrder() {}

    public PetCareOrder(PetCareItemData item, LocalDate date) {
        this.item = item;
        this.date = date;
    }

    public PetCareItemData getItem() {
        return item;
    }

    public void setItem(PetCareItemData item) {
        this.item = item;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
