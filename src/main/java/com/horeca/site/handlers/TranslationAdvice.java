package com.horeca.site.handlers;

import com.horeca.site.models.hotel.translation.LanguageCode;
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

    @Around(value = "@annotation(TranslateReturnValue)")
    public Object translate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object objectToTranslate = joinPoint.proceed();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();

        int hotelIdIndex = 0;
        while (hotelIdIndex < parameters.length) {
            Parameter current = parameters[hotelIdIndex];
            if (current.getAnnotation(HotelId.class) != null) {
                break;
            }
            hotelIdIndex++;
        }

        if (hotelIdIndex == parameters.length) {
            logger.error("Couldn't extract hotelId when processing " + request.getRequestURI());
            return objectToTranslate;
        }

        Object[] args = joinPoint.getArgs();
        Long hotelId = (Long) args[hotelIdIndex];

        LanguageCode languageCode = LanguageCodeArgumentResolver.resolveFromLocale(request.getLocale());
        try {
            return translationService.translate(objectToTranslate, hotelId, languageCode);
        } catch (Exception ex) {
            logger.error("There was a problem when translating " + objectToTranslate, ex);
            return objectToTranslate;
        }
    }
}
