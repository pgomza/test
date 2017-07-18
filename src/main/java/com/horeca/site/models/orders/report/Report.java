package com.horeca.site.models.orders.report;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Report {

    @NotNull
    private ReportGuest reportGuest;

    @NotNull
    private List<ChargeDetails> chargeDetails = new ArrayList<>();

    @NotEmpty
    private String totalAmount;

    public Report(ReportGuest reportGuest, List<ChargeDetails> chargeDetails, String totalAmount) {
        this.reportGuest = reportGuest;
        this.chargeDetails = chargeDetails;
        this.totalAmount = totalAmount;
    }

    public ReportGuest getReportGuest() {
        return reportGuest;
    }

    public void setReportGuest(ReportGuest reportGuest) {
        this.reportGuest = reportGuest;
    }

    public List<ChargeDetails> getChargeDetails() {
        return chargeDetails;
    }

    public void setChargeDetails(List<ChargeDetails> chargeDetails) {
        this.chargeDetails = chargeDetails;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
