package com.horeca.site.models.orders.report;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ChargeDetails {

    @NotNull
    private String serviceName;

    private String usageFee;

    @NotNull
    private List<ReportOrder> orders = new ArrayList<>();

    public ChargeDetails(String serviceName, String usageFee, List<ReportOrder> orders) {
        this.serviceName = serviceName;
        this.usageFee = usageFee;
        this.orders = orders;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUsageFee() {
        return usageFee;
    }

    public void setUsageFee(String usageFee) {
        this.usageFee = usageFee;
    }

    public List<ReportOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ReportOrder> orders) {
        this.orders = orders;
    }
}
