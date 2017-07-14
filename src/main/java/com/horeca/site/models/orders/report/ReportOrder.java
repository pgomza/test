package com.horeca.site.models.orders.report;

import org.hibernate.validator.constraints.NotEmpty;

public class ReportOrder {

    @NotEmpty
    private String description;

    @NotEmpty
    private String amount;

    public ReportOrder(String description, String amount) {
        this.description = description;
        this.amount = amount;
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
}
