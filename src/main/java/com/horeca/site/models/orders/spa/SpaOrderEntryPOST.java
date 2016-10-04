package com.horeca.site.models.orders.spa;

import javax.validation.constraints.NotNull;

public class SpaOrderEntryPOST {

    @NotNull
    private Long id;

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
