package com.horeca.site.models.orders.report;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Report {

    @NotNull
    private ReportGuest guest;

    @NotNull
    private List<ChargeDetails> chargeDetails = new ArrayList<>();

    @NotEmpty
    private String totalAmount;

    public Report(ReportGuest guest, List<ChargeDetails> chargeDetails, String totalAmount) {
        this.guest = guest;
        this.chargeDetails = chargeDetails;
        this.totalAmount = totalAmount;
    }

    public ReportGuest getGuest() {
        return guest;
    }

    public void setGuest(ReportGuest guest) {
        this.guest = guest;
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
