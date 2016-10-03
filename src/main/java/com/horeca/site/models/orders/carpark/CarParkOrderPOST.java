package com.horeca.site.models.orders.carpark;

import javax.validation.constraints.NotNull;

public class CarParkOrderPOST {

    @NotNull
    private String licenseNumber;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
