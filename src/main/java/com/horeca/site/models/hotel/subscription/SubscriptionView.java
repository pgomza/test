package com.horeca.site.models.hotel.subscription;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class SubscriptionView {

    @NotNull
    private Integer currentLevel;

    private Timestamp expiresAt;

    @NotNull
    private Boolean trialEligible;

    SubscriptionView() {}

    public SubscriptionView(Integer currentLevel, Timestamp expiresAt, Boolean trialEligible) {
        this.currentLevel = currentLevel;
        this.expiresAt = expiresAt;
        this.trialEligible = trialEligible;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getTrialEligible() {
        return trialEligible;
    }

    public void setTrialEligible(Boolean trialEligible) {
        this.trialEligible = trialEligible;
    }
}