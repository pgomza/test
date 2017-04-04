package com.horeca.site.models.hotel.services;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.horeca.site.models.Price;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class AvailableServiceViewSimplified {

    private AvailableServiceType type;

    private Price price;

    private String additionalInfo;

    public AvailableServiceType getType() {
        return type;
    }

    public void setType(AvailableServiceType type) {
        this.type = type;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
