package com.horeca.site.models.hotel.subscription;

public enum SubscriptionLevel {
    BASIC(1),
    PREMIUM(2);

    private final int number;

    SubscriptionLevel(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
