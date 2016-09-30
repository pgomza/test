package com.horeca.site.models.orders.carpark;

import javax.validation.constraints.NotNull;

public class CarParkOrderPOST {

    @NotNull
    private String licensePlate;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
