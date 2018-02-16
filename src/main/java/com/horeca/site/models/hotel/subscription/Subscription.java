package com.horeca.site.models.hotel.subscription;

import com.horeca.site.models.hotel.Hotel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @NotNull
    private Boolean trialEligible;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "subscription_id")
    private List<SubscriptionEvent> history = new ArrayList<>();

    Subscription() {}

    public Subscription(Hotel hotel, Boolean trialEligible, List<SubscriptionEvent> history) {
        this.hotel = hotel;
        this.trialEligible = trialEligible;
        this.history = history;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Boolean getTrialEligible() {
        return trialEligible;
    }

    public void setTrialEligible(Boolean trialEligible) {
        this.trialEligible = trialEligible;
    }

    public List<SubscriptionEvent> getHistory() {
        return history;
    }

    public void setHistory(List<SubscriptionEvent> history) {
        this.history = history;
    }
}
