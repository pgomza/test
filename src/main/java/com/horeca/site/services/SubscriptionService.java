package com.horeca.site.services;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.subscription.Subscription;
import com.horeca.site.models.hotel.subscription.SubscriptionEvent;
import com.horeca.site.models.hotel.subscription.SubscriptionLevel;
import com.horeca.site.models.hotel.subscription.SubscriptionView;
import com.horeca.site.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriptionService {

    @Value("${subscription.premiumValidityPeriod}")
    private Integer premiumValidityPeriod;

    @Value("${subscription.trialValidityPeriod}")
    private Integer trialValidityPeriod;

    @Autowired
    private SubscriptionRepository repository;

    @Autowired
    private HotelQueryService hotelQueryService;

    private Subscription createIfDoesntExistAndGet(Long hotelId) {
        Subscription subscription = repository.findByHotelId(hotelId);
        if (subscription == null) {
            Hotel hotel = hotelQueryService.get(hotelId);
            subscription = repository.save(new Subscription(hotel, true, Collections.emptyList()));
        }
        return subscription;
    }

    public SubscriptionView getView(Long hotelId) {
        Subscription subscription = createIfDoesntExistAndGet(hotelId);
        Boolean trialEligible = subscription.getTrialEligible();
        List<SubscriptionEvent> history = subscription.getHistory();

        if (!history.isEmpty()) {
            SubscriptionEvent lastEvent = history.get(history.size() - 1);
            if (lastEvent.getExpiresAt().after(new Date())) {
                return new SubscriptionView(lastEvent.getLevel(), lastEvent.getExpiresAt(), trialEligible);
            }
        }
        return new SubscriptionView(SubscriptionLevel.BASIC.getNumber(), null, trialEligible);
    }

    public SubscriptionEvent addPremiumEvent(Long hotelId, Integer level) {
        if (!isLevelSupported(level)) {
            throw new BusinessRuleViolationException("Unsupported level of a premium subscription");
        }

        Subscription subscription = createIfDoesntExistAndGet(hotelId);
        List<SubscriptionEvent> history = subscription.getHistory();

        Timestamp oldExpirationTimestamp = null;
        Timestamp newExpirationTimestamp;

        if (!history.isEmpty()) {
            SubscriptionEvent lastEvent = history.get(history.size() - 1);
            if (lastEvent.getExpiresAt().after(new Date())) {
                oldExpirationTimestamp = lastEvent.getExpiresAt();
            }
        }

        if (oldExpirationTimestamp == null) {
            long currentTime = new Date().getTime();
            newExpirationTimestamp = new Timestamp(currentTime + premiumValidityPeriod * (1000L * 60 * 60 * 24));
        }
        else {
            newExpirationTimestamp = new Timestamp(
                    oldExpirationTimestamp.getTime() + premiumValidityPeriod * (1000L * 60 * 60 * 24)
            );
        }

        SubscriptionEvent newEvent = new SubscriptionEvent(level, premiumValidityPeriod, newExpirationTimestamp);
        history.add(newEvent);
        repository.save(subscription);
        return history.get(history.size() - 1);
    }

    private boolean isLevelSupported(Integer level) {
        Set<Integer> supportedLevels = Arrays.stream(SubscriptionLevel.values())
                .map(SubscriptionLevel::getNumber)
                .collect(Collectors.toSet());
        supportedLevels.remove(SubscriptionLevel.BASIC.getNumber());
        return supportedLevels.contains(level);
    }
}
