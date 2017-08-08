package com.horeca.site.models.orders.report;

import org.hibernate.validator.constraints.NotEmpty;

public class ReportGuest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String roomNumber;

    @NotEmpty
    private String arrival;

    @NotEmpty
    private String departure;

    public ReportGuest(String name, String roomNumber, String arrival, String departure) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.arrival = arrival;
        this.departure = departure;
    }

    public String getName() {
        return name;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
        return departure;
    }
}
