package com.horeca.site.models.orders.spa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.hotel.services.spa.SpaItem;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "orders_id_spa", columnList = "orders_id_spa"))
@Audited
public class SpaOrder extends Order {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private SpaItem item;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

    public SpaItem getItem() {
        return item;
    }

    public void setItem(SpaItem item) {
        this.item = item;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
