package com.horeca.site.models.orders.breakfast;

import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.breakfast.BreakfastItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class BreakfastOrderEntry implements Viewable<BreakfastOrderEntryView> {

    @Id
    @GeneratedValue
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

    @Override
    public BreakfastOrderEntryView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastOrderEntryView view = new BreakfastOrderEntryView();
        view.setItem(getItem().toView(preferredLanguage, defaultLanguage));
        view.setCount(getCount());
        return view;
    }
}
