package com.horeca.site.models.orders.taxi;

import javax.validation.constraints.NotNull;

public class TaxiOrderPOST {

    @NotNull
    private String time;

    private String destination;

    private Integer numberOfPeople;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
