package com.horeca.site.handlers;

import com.horeca.site.models.updates.ChangeInHotelEvent;
import com.horeca.site.models.updates.ChangeInStayEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.horeca.utils.UrlPartExtractors.*;

@Component
public class UpdatesInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(UpdatesInterceptor.class);

    // there's also the PATCH method but it's not supported by any of the exposed endpoints
    private static final Set<String> updateHttpMethods = new HashSet<>(Arrays.asList(
           "POST", "PUT", "DELETE"
    ));

    // analogous to the above
    private static final Set<Integer> successfulHttpStatuses = new HashSet<>(Arrays.asList(
            200, 201, 202, 204
    ));

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if ("DELETE".equals(method)) {
            String pin = extractStayPinFromServletPath(request.getRequestURI(), stayPinPatternExact);
            if (pin != null) {
                markAsHandled(response);
                notifyHotel(pin);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (!isAlreadyHandled(response)) {
            Integer statusCode = response.getStatus();
            String method = request.getMethod();

            if (successfulHttpStatuses.contains(statusCode) && updateHttpMethods.contains(method)) {

                String uri = request.getRequestURI();
                // since the patterns are already precompiled the extractions should be really fast
                String pin = extractStayPinFromServletPath(uri, stayPinPattern);
                pin = (pin != null) ? pin : extractStayPinFromServletPath(uri, checkInPattern);
                pin = (pin != null) ? pin : extractStayPinFromServletPath(uri, checkOutPattern);

                if (pin != null) {
                    notifyHotel(pin);
                } else if ("/api/stays".equals(uri)) {
                    String location = response.getHeader("Location");
                    if (location != null) {
                        pin = extractStayPinFromServletPath(location, stayPinPattern);
                        if (pin != null) {
                            notifyHotel(pin);
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

    private void markAsHandled(HttpServletResponse response) {
        response.setHeader("notified", "true");
    }

    private boolean isAlreadyHandled(HttpServletResponse response) {
        return response.getHeader("notified") != null;
    }

    private void notifyHotel(Long hotelId) {
        logger.debug("Change in hotel: " + hotelId);
        eventPublisher.publishEvent(new ChangeInHotelEvent(this, hotelId));
    }

    private void notifyHotel(String pin) {
        logger.debug("Change in stay: " + pin);
        eventPublisher.publishEvent(new ChangeInStayEvent(this, pin));
    }
}
