package com.horeca.site.handlers;

import com.horeca.site.models.hotel.translation.LanguageCode;
import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.services.translation.HotelTranslationService;
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
public class TranslationAdvice extends HotelAdvice {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final HotelTranslationService translationService;

    @Autowired
    public TranslationAdvice(StayRepository stayRepository, HotelTranslationService translationService) {
        super(stayRepository);
        this.translationService = translationService;
    }

    @Around(value = "@annotation(TranslateReturnValue)")
    public Object translate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object objectToTranslate = joinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        Optional<Long> hotelIdOpt = extractHotelId(joinPoint);
        if (!hotelIdOpt.isPresent()) {
            logger.error("Couldn't extract hotelId when processing " + request.getRequestURI());
            return objectToTranslate;
        }

        LanguageCode languageCode = LanguageCodeArgumentResolver.resolveFromLocale(request.getLocale());
        try {
            return translationService.translate(objectToTranslate, hotelIdOpt.get(), languageCode);
        } catch (Exception ex) {
            logger.error("There was a problem when translating " + objectToTranslate, ex);
            return objectToTranslate;
        }
    }
}
