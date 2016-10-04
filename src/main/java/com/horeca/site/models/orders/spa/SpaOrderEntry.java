package com.horeca.site.models.orders.spa;

import com.horeca.site.models.Viewable;
import com.horeca.site.models.hotel.services.spa.SpaItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SpaOrderEntry implements Viewable<SpaOrderEntryView> {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SpaOrderEntry_id")
    private SpaItem item;

    @NotNull
    private String day;

    @NotNull
    private String hour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SpaItem getItem() {
        return item;
    }

    public void setItem(SpaItem item) {
        this.item = item;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public SpaOrderEntryView toView(String preferredLanguage, String defaultLanguage) {
        SpaOrderEntryView view = new SpaOrderEntryView();
        view.setDay(getDay());
        view.setHour(getHour());
        view.setItem(getItem().toView(preferredLanguage, defaultLanguage));
        return view;
    }
}
