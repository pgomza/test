package com.horeca.site.models.orders.tableordering;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.orders.OrderStatus;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "orders_id_tableordering", columnList = "orders_id_tableordering"))
public class TableOrderingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private OrderStatus status;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

    @NotNull
    private Integer numberOfPeople;

    TableOrderingOrder() {
    }

    public TableOrderingOrder(LocalDateTime time, Integer numberOfPeople) {
        this(OrderStatus.NEW, time, numberOfPeople);
    }

    public TableOrderingOrder(OrderStatus status, LocalDateTime time, Integer numberOfPeople) {
        this.status = status;
        this.time = time;
        this.numberOfPeople = numberOfPeople;
    }

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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
