package com.horeca.site.services.subscription;

import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.subscription.*;
import com.horeca.site.repositories.SubscriptionRepository;
import com.horeca.site.services.HotelQueryService;
import com.horeca.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public int getCurrentLevel(Long hotelId) {
        Integer currentLevel = repository.getCurrentLevel(hotelId);
        if (currentLevel == null) {
            currentLevel = SubscriptionLevel.BASIC.getNumber();
        }
        return currentLevel;
    }

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
        Integer currentLevel = getCurrentLevel(hotelId);
        return new SubscriptionView(currentLevel, trialEligible);
    }

    public Page<SubscriptionEventView> getHistoryView(Long hotelId, Pageable pageable) {
        Subscription subscription = createIfDoesntExistAndGet(hotelId);
        List<SubscriptionEvent> events = subscription.getHistory();
        // sort the entries by expiration time (in descending order)
        events.sort((a, b) -> {
            if (a.getExpiresAt().after(b.getExpiresAt())) {
                return -1;
            }
            else {
                return 1;
            }
        });
        List<SubscriptionEventView> eventViews = events.stream()
                .map(SubscriptionEvent::toView)
                .collect(Collectors.toList());
        return PageableUtils.extractPage(eventViews, pageable);
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

    public SubscriptionEvent enableTrial(Long hotelId) {
        Subscription subscription = createIfDoesntExistAndGet(hotelId);
        if (!subscription.getTrialEligible()) {
            throw new BusinessRuleViolationException("You are not eligible for the trial subscription anymore");
        }
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
            newExpirationTimestamp = new Timestamp(currentTime + trialValidityPeriod * (1000L * 60 * 60 * 24));
        }
        else {
            newExpirationTimestamp = new Timestamp(
                    oldExpirationTimestamp.getTime() + trialValidityPeriod * (1000L * 60 * 60 * 24)
            );
        }

        SubscriptionLevel highestLevel = getHighestLevel();
        SubscriptionEvent newEvent = new SubscriptionEvent(highestLevel.getNumber(), trialValidityPeriod,
                newExpirationTimestamp);
        history.add(newEvent);
        subscription.setTrialEligible(false);
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

    private SubscriptionLevel getHighestLevel() {
        SubscriptionLevel[] levels = SubscriptionLevel.values();
        if (levels.length < 2) { // this should never be the case though; probably a better option would be to use
            // an assertion
            throw new RuntimeException("At least 2 subscription levels should be supported");
        }
        return levels[levels.length - 1];
    }
}
