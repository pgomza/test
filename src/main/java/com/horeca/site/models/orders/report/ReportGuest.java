package com.horeca.site.models.orders.report;

import org.hibernate.validator.constraints.NotEmpty;

public class ReportGuest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String roomNumber;

    public ReportGuest(String name, String roomNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
