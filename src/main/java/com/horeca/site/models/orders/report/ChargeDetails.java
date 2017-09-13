package com.horeca.site.models.orders.report;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ChargeDetails {

    @NotNull
    private String serviceName;

    @NotNull
    private List<ReportOrder> orders = new ArrayList<>();

    public ChargeDetails(String serviceName, List<ReportOrder> orders) {
        this.serviceName = serviceName;
        this.orders = orders;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<ReportOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ReportOrder> orders) {
        this.orders = orders;
    }
}
