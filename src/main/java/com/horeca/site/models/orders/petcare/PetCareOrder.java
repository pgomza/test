package com.horeca.site.models.orders.petcare;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.orders.OrderStatus;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
