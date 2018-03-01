package com.horeca.site.models.orders.rental;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "orders_id_rental", columnList = "orders_id_rental"))
@Audited
public class RentalOrder extends Order {

    @NotNull
    private Price total;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "rental_order_id")
    private Set<RentalOrderItem> items;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime time;

    RentalOrder() {}

    public RentalOrder(Price total, Set<RentalOrderItem> items, LocalDateTime time) {
        this.total = total;
        this.items = items;
        this.time = time;
    }

    public Price getTotal() {
        return total;
    }

    public void setTotal(Price total) {
        this.total = total;
    }

    public Set<RentalOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<RentalOrderItem> items) {
        this.items = items;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
