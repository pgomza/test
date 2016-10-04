package com.horeca.site.models.orders.taxi;

import javax.validation.constraints.NotNull;

public class TaxiOrderPOST {

    @NotNull
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
