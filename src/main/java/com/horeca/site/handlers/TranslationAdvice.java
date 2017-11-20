package com.horeca.site.handlers;

import com.horeca.site.models.hotel.translation.LanguageCode;
import com.horeca.site.repositories.services.StayRepository;
import com.horeca.site.services.translation.HotelTranslationService;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class TranslationAdvice {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private HotelTranslationService translationService;

    @Autowired
    private StayRepository stayRepository;

    @Around(value = "@annotation(TranslateReturnValue)")
    public Object translate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object objectToTranslate = joinPoint.proceed();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        Object[] methodArgs = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();

        boolean foundHotelId = false;
        Long hotelId = null;
        int i = 0;
        while (i < parameters.length && !foundHotelId) {
            Parameter param = parameters[i];
            if (param.getAnnotation(HotelId.class) != null) {
                hotelId = (Long) methodArgs[i];
                foundHotelId = true;
            }
            else if (param.getAnnotation(StayPin.class) != null) {
                String pin = (String) methodArgs[i];
                hotelId = stayRepository.getHotelIdOfStay(pin);
                foundHotelId = true;
            }
            i++;
        }

        if (!foundHotelId) {
            logger.error("Couldn't extract hotelId when processing " + request.getRequestURI());
            return objectToTranslate;
        }

        LanguageCode languageCode = LanguageCodeArgumentResolver.resolveFromLocale(request.getLocale());
        try {
            return translationService.translate(objectToTranslate, hotelId, languageCode);
        } catch (Exception ex) {
            logger.error("There was a problem when translating " + objectToTranslate, ex);
            return objectToTranslate;
        }
    }
}
