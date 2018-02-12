package com.horeca.site.models.hotel.subscription;

import javax.validation.constraints.NotNull;

public class SubscriptionView {

    @NotNull
    private Integer currentLevel;

    @NotNull
    private Boolean trialEligible;

    SubscriptionView() {}

    public SubscriptionView(Integer currentLevel, Boolean trialEligible) {
        this.currentLevel = currentLevel;
        this.trialEligible = trialEligible;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Boolean getTrialEligible() {
        return trialEligible;
    }

    public void setTrialEligible(Boolean trialEligible) {
        this.trialEligible = trialEligible;
    }
}