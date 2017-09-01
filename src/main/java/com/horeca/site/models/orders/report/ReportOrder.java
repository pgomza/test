package com.horeca.site.models.orders.report;

import org.hibernate.validator.constraints.NotEmpty;

public class ReportOrder {

    @NotEmpty
    private String description;

    @NotEmpty
    private String amount;

    @NotEmpty
    private String placedAt;

    public ReportOrder(String description, String amount, String placedAt) {
        this.description = description;
        this.amount = amount;
        this.placedAt = placedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(String placedAt) {
        this.placedAt = placedAt;
    }
}
