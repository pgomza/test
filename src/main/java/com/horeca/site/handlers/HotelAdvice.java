package com.horeca.site.handlers;

import com.horeca.site.repositories.services.StayRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public abstract class HotelAdvice {

    protected final StayRepository stayRepository;

    public HotelAdvice(StayRepository stayRepository) {
        this.stayRepository = stayRepository;
    }

    public Optional<Long> extractHotelId(JoinPoint joinPoint) {
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

        return Optional.ofNullable(hotelId);
    }
}
