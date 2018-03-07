package com.horeca.site.models.orders.roomservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.translation.Translatable;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;
import org.joda.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "orders_id_room_service", columnList = "orders_id_room_service"))
@Audited
public class RoomServiceOrder extends Order {

    @NotNull
    private Price total;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime time;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "room_service_order_id")
    private Set<RoomServiceOrderItem> items;

    RoomServiceOrder() {}

    public RoomServiceOrder(Price total, LocalTime time, Set<RoomServiceOrderItem> items) {
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Set<RoomServiceOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<RoomServiceOrderItem> items) {
        this.items = items;
    }
}
