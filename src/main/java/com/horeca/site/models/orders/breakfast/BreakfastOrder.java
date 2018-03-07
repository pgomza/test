package com.horeca.site.models.orders.breakfast;

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
@Table(indexes = @Index(name = "orders_id_breakfast", columnList = "orders_id_breakfast"))
@Audited
public class BreakfastOrder extends Order {

    @NotNull
    private Price total;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "breakfast_order_id")
    private Set<BreakfastOrderItem> items;

    BreakfastOrder() {}

    public BreakfastOrder(Price total, LocalDateTime time, Set<BreakfastOrderItem> items) {
        this.total = total;
        this.time = time;
        this.items = items;
    }

    public Price getTotal() {
        return total;
    }

    public void setTotal(Price total) {
        this.total = total;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Set<BreakfastOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastOrderItem> items) {
        this.items = items;
    }
}
