package com.horeca.site.handlers;

import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.services.subscription.SubscriptionService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
@Transactional
public class SubscriptionAdvice extends HotelAdvice {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionAdvice(StayRepository stayRepository, SubscriptionService subscriptionService) {
        super(stayRepository);
        this.subscriptionService = subscriptionService;
    }

    @Before(value = "@annotation(MinSubscriptionLevel)")
    public void checkIfAllowed(JoinPoint joinPoint) {
        Optional<Long> hotelIdOpt = extractHotelId(joinPoint);
        if (!hotelIdOpt.isPresent()) { // should never happen though
            logger.error("Could not extract hotel id from join point " + joinPoint.toString());
            throw new RuntimeException("The server encountered an unexpected condition that prevented it from " +
                    "fulfilling the request");
        }

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        MinSubscriptionLevel annotation = method.getAnnotation(MinSubscriptionLevel.class);
        int minRequiredLevel = annotation.value();
        int actualLevel = subscriptionService.getCurrentLevel(hotelIdOpt.get());
        if (actualLevel < minRequiredLevel) {
            throw new AccessDeniedException("Insufficient subscription level");
        }
    }
}
