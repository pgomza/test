package com.horeca.site.models.hotel.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.horeca.site.models.Price;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
