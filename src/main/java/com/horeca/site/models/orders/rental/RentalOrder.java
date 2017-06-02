package com.horeca.site.models.orders.rental;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import com.horeca.site.models.orders.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "orders_id_rental", columnList = "orders_id_rental"))
public class RentalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    @NotNull
    private Price total;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "rental_order_id")
    private Set<RentalOrderItem> items;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime time;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
