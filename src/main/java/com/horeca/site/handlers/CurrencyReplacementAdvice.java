package com.horeca.site.handlers;

import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.services.CurrencyReplacementService;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Aspect
@Component
@Transactional
public class CurrencyReplacementAdvice extends HotelAdvice {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final CurrencyReplacementService service;

    @Autowired
    public CurrencyReplacementAdvice(StayRepository stayRepository, CurrencyReplacementService service) {
        super(stayRepository);
        this.service = service;
    }

    @Around(value = "@annotation(ReplaceCurrency)")
    public Object replace(ProceedingJoinPoint joinPoint) throws Throwable {
        Object originalObject = joinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        Optional<Long> hotelIdOpt = extractHotelId(joinPoint);
        if (!hotelIdOpt.isPresent()) {
            logger.error("Couldn't extract hotelId when processing " + request.getRequestURI());
            return originalObject;
        }

        return service.replace(originalObject, hotelIdOpt.get());
    }
}
