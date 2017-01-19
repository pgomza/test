package com.horeca.site.models.orders.breakfast;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import com.horeca.site.models.orders.OrderStatus;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "orders_id_breakfast", columnList = "orders_id_breakfast"))
public class BreakfastOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    @NotNull
    private Price total;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "breakfast_order_id")
    private Set<BreakfastOrderItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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
