package com.horeca.site.models.orders.breakfast;

import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "breakfast_order_id", columnList = "breakfast_order_id"))
public class BreakfastOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private BreakfastItem item;

    @NotNull
    private Integer count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BreakfastItem getItem() {
        return item;
    }

    public void setItem(BreakfastItem item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
