package com.horeca.site.models.orders.petcare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.orders.OrderStatus;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "orders_id", columnList = "orders_id"))
public class PetCareOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private PetCareItem item;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

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

    public PetCareItem getItem() {
        return item;
    }

    public void setItem(PetCareItem item) {
        this.item = item;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
