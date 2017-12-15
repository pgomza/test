package com.horeca.site.handlers;

import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.services.CurrencyReplacementService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class CurrencyReplacementAdvice extends HotelAdvice {

    private final CurrencyReplacementService service;

    @Autowired
    public CurrencyReplacementAdvice(StayRepository stayRepository, CurrencyReplacementService service) {
        super(stayRepository);
        this.service = service;
    }

    @Around(value = "@annotation(ReplaceCurrency)")
    public Object replace(ProceedingJoinPoint joinPoint) throws Throwable {
        Object originalObject = joinPoint.proceed();

        Optional<Long> hotelIdOpt = extractHotelId(joinPoint);
        service.replace(originalObject, ho)
    }
}
