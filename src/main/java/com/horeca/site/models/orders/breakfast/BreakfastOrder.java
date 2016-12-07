package com.horeca.site.models.orders.breakfast;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horeca.site.models.Price;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.orders.OrderStatus;
import org.joda.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BreakfastOrder implements Viewable<BreakfastOrderView> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    @NotNull
    private Price total;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime time;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Set<BreakfastOrderItem> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastOrderItem> items) {
        this.items = items;
    }

    @Override
    public BreakfastOrderView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastOrderView view = new BreakfastOrderView();
        view.setId(getId());
        view.setStatus(getStatus());
        view.setTotal(getTotal());
        view.setTime(getTime().toString("HH:mm"));

        Set<BreakfastOrderItemView> entryViews = new HashSet<>();
        for (BreakfastOrderItem entry : getItems()) {
            entryViews.add(entry.toView(preferredLanguage, defaultLanguage));
        }
        view.setEntries(entryViews);

        return view;
    }
}
