package com.horeca.site.models.orders.bar;

import com.horeca.site.models.hotel.services.bar.BarItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = @Index(name = "bar_order_id", columnList = "bar_order_id"))
public class BarOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private BarItem item;

    @NotNull
    private Integer count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BarItem getItem() {
        return item;
    }

    public void setItem(BarItem item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
