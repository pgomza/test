package com.horeca.site.models.orders.breakfast;

import com.horeca.site.models.Viewable;
import com.horeca.site.models.orders.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BreakfastOrder implements Viewable<BreakfastOrderView> {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private OrderStatus status = OrderStatus.NEW;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn
    private Set<BreakfastOrderEntry> entries;

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

    public Set<BreakfastOrderEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<BreakfastOrderEntry> entries) {
        this.entries = entries;
    }

    @Override
    public BreakfastOrderView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastOrderView view = new BreakfastOrderView();
        view.setId(getId());
        view.setStatus(getStatus());

        Set<BreakfastOrderEntryView> entryViews = new HashSet<>();
        for (BreakfastOrderEntry entry : getEntries()) {
            entryViews.add(entry.toView(preferredLanguage, defaultLanguage));
        }
        view.setEntries(entryViews);

        return view;
    }
}
