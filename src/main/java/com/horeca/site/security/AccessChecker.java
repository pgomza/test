package com.horeca.site.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccessChecker {

    private final Logger logger = Logger.getLogger(AccessChecker.class);
    private final Pattern hotelIdPattern = Pattern.compile("/api/hotels/(\\d+)");

    public boolean checkForHotel(Authentication authentication, HttpServletRequest request) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccount) {
            String servletPath = request.getServletPath();
            Long hotelId = extractHotelIdFromServletPath(servletPath);

            if (hotelId != null) {
                UserAccount userAccount = (UserAccount) principal;
                if (hotelId.equals(userAccount.getHotelId()))
                    return true;
            }
        }
        return false;
    }

    private Long extractHotelIdFromServletPath(String servletPath) {
        Matcher matcher = hotelIdPattern.matcher(servletPath);
        Long result = null;
        if (matcher.find() && matcher.groupCount() == 1) {
            try {
                result = Long.valueOf(matcher.group(1));
            }
            catch (NumberFormatException ex) {
                logger.warn("Could not parse a hotel id from the request path " + servletPath);
                throw ex;
            }
        }
        return result;
    }
}
