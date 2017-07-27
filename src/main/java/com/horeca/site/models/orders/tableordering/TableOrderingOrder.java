package com.horeca.site.models.orders.tableordering;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.orders.Order;
import org.joda.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "orders_id_tableordering", columnList = "orders_id_tableordering"))
public class TableOrderingOrder extends Order{

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime time;

    @NotNull
    private Integer numberOfPeople;

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
