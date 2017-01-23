package com.horeca.site.models.orders.carpark;

import javax.validation.constraints.NotNull;

public class CarParkOrderPOST {

    @NotNull
    private String licenseNumber;

    @NotNull
    private String fromDate;

    @NotNull
    private String toDate;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
