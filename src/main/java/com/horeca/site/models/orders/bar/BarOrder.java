package com.horeca.site.models.orders.bar;

import com.horeca.site.models.Price;
import com.horeca.site.models.orders.Order;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "orders_id_bar", columnList = "orders_id_bar"))
@Audited
public class BarOrder extends Order {

    @NotNull
    private Price total;

    @NotEmpty
    private String tableNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "bar_order_id")
    private Set<BarOrderItem> items;

    public Price getTotal() {
        return total;
    }

    public void setTotal(Price total) {
        this.total = total;
    }

    public Set<BarOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<BarOrderItem> items) {
        this.items = items;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }
}
