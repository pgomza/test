package com.horeca.site.models.orders.spa;

import com.horeca.site.models.hotel.services.spa.SpaItemView;

public class SpaOrderEntryView {

    private SpaItemView item;

    private String day;

    private String hour;

    public SpaItemView getItem() {
        return item;
    }

    public void setItem(SpaItemView item) {
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
}
