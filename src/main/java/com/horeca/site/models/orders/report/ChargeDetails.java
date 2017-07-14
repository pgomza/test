package com.horeca.site.models.orders.report;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ChargeDetails {

    @NotNull
    private String serviceName;

    @NotEmpty
    private String usageFee;

    @NotNull
    private List<ReportOrder> reportOrders = new ArrayList<>();

    public ChargeDetails(String serviceName, String usageFee, List<ReportOrder> reportOrders) {
        this.serviceName = serviceName;
        this.usageFee = usageFee;
        this.reportOrders = reportOrders;
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

    public List<ReportOrder> getReportOrders() {
        return reportOrders;
    }

    public void setReportOrders(List<ReportOrder> reportOrders) {
        this.reportOrders = reportOrders;
    }
}
