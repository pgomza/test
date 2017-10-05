package com.horeca.site.handlers;

import com.horeca.site.models.updates.ChangeInHotelEvent;
import com.horeca.site.repositories.services.StayRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.horeca.utils.UrlPartExtractors.*;

@Component
public class UpdatesInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(UpdatesInterceptor.class);

    private static final Set<String> updateHttpMethods = new HashSet<>(Arrays.asList(
           "POST", "PUT", "PATCH", "DELETE"
    ));

    // analogous to the above
    private static final Set<Integer> successfulHttpStatuses = new HashSet<>(Arrays.asList(
            200, 201, 202, 204
    ));

    private static final String POSTPONED_HOTEL_ID_HEADER = "Interceptor";

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if ("DELETE".equals(method)) {
            String pin = extractStayPinFromServletPath(request.getRequestURI(), stayPinPatternExact);
            if (pin != null) {
                Optional<Long> hotelIdOpt = getHotelIdFromPin(pin);
                hotelIdOpt.ifPresent(hotelId -> setPostponedHotelId(response, hotelId));
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Optional<Long> postponedHotelIdOpt = getPostponedHotelId(response);
        Integer statusCode = response.getStatus();
        String method = request.getMethod();

        if (successfulHttpStatuses.contains(statusCode) && updateHttpMethods.contains(method)) {
            if (postponedHotelIdOpt.isPresent()) {
                notifyHotel(postponedHotelIdOpt.get());
            }
            else {
                String uri = request.getRequestURI();
                // since the patterns are already precompiled the extractions should be really fast
                String pin = extractStayPinFromServletPath(uri, stayPinPattern);
                pin = (pin != null) ? pin : extractStayPinFromServletPath(uri, checkInPattern);
                pin = (pin != null) ? pin : extractStayPinFromServletPath(uri, checkOutPattern);

                if (pin != null) {
                    Optional<Long> hotelIdOpt = getHotelIdFromPin(pin);
                    hotelIdOpt.ifPresent(this::notifyHotel);
                } else if ("/api/stays".equals(uri)) {
                    String location = response.getHeader("Location");
                    if (location != null) {
                        pin = extractStayPinFromServletPath(location, stayPinPattern);
                        if (pin != null) {
                            Optional<Long> hotelIdOpt = getHotelIdFromPin(pin);
                            hotelIdOpt.ifPresent(this::notifyHotel);
                        }
                    }
                } else {
                    Long hotelId = extractIdFromServletPath(request.getRequestURI(), hotelIdPattern);
                    if (hotelId != null) {
                        notifyHotel(hotelId);
                    }
                }
            }
        }
    }

    private void notifyHotel(Long hotelId) {
        logger.debug("Change in hotel: " + hotelId);
        eventPublisher.publishEvent(new ChangeInHotelEvent(this, hotelId));
    }

    private Optional<Long> getHotelIdFromPin(String pin) {
        Long hotelId = stayRepository.getHotelIdOfStay(pin);
        return Optional.ofNullable(hotelId);
    }

    private void setPostponedHotelId(HttpServletResponse response, Long hotelId) {
        response.setHeader(POSTPONED_HOTEL_ID_HEADER, hotelId.toString());
    }

    private Optional<Long> getPostponedHotelId(HttpServletResponse response) {
        String header = response.getHeader(POSTPONED_HOTEL_ID_HEADER);
        Optional<Long> result;
        if (header != null) {
            result = Optional.of(Long.valueOf(header));
        }
        else {
            result = Optional.empty();
        }
        response.setHeader(POSTPONED_HOTEL_ID_HEADER, "handled");
        return result;
    }
}
