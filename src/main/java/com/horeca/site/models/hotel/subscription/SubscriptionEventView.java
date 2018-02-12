package com.horeca.site.models.hotel.subscription;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SubscriptionEventView {

    @NotNull
    private Integer level;

    @NotEmpty
    private String createdAt;

    @NotNull
    private Integer validityPeriod;

    @NotEmpty
    private String expiresAt;

    SubscriptionEventView() {}

    public SubscriptionEventView(Integer level, String createdAt, Integer validityPeriod, String expiresAt) {
        this.level = level;
        this.createdAt = createdAt;
        this.validityPeriod = validityPeriod;
        this.expiresAt = expiresAt;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
