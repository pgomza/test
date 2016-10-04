package com.horeca.site.models.orders.spa;

import com.horeca.site.models.Price;
import com.horeca.site.models.Viewable;
import com.horeca.site.models.orders.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SpaOrder implements Viewable<SpaOrderView> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    @NotNull
    private Price total;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<SpaOrderEntry> items;

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

    public Set<SpaOrderEntry> getItems() {
        return items;
    }

    public void setItems(Set<SpaOrderEntry> items) {
        this.items = items;
    }

    @Override
    public SpaOrderView toView(String preferredLanguage, String defaultLanguage) {
        SpaOrderView view = new SpaOrderView();
        view.setId(getId());
        view.setStatus(getStatus());
        view.setTotal(getTotal());

        Set<SpaOrderEntryView> entryViews = new HashSet<>();
        for (SpaOrderEntry entry : getItems()) {
            SpaOrderEntryView entryView = entry.toView(preferredLanguage, defaultLanguage);
            entryViews.add(entryView);
        }
        view.setItems(entryViews);

        return view;
    }
}
