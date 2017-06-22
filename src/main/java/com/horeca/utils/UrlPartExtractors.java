package com.horeca.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlPartExtractors {

    public static final Pattern hotelIdPattern = Pattern.compile("/api/hotels/(\\d+)");
    public static final Pattern stayPinPattern = Pattern.compile("/api/stays/([a-z0-9]+)");
    public static final Pattern checkInPattern = Pattern.compile("/api/check-in/([a-z0-9]+)");
    public static final Pattern checkOutPattern = Pattern.compile("/api/check-out/([a-z0-9]+)");

    public static Long extractIdFromServletPath(String servletPath, Pattern pattern) {
        Matcher matcher = pattern.matcher(servletPath);
        Long result = null;
        if (matcher.find() && matcher.groupCount() == 1) {
            try {
                result = Long.valueOf(matcher.group(1));
            }
            catch (NumberFormatException ex) {
                throw ex;
            }
        }
        return result;
    }

    public static String extractStayPinFromServletPath(String servletPath, Pattern pattern) {
        Matcher matcher = pattern.matcher(servletPath);
        String result = null;
        if (matcher.find() && matcher.groupCount() == 1) {
            try {
                result = matcher.group(1);
            }
            catch (NumberFormatException ex) {
                throw ex;
            }
        }
        return result;
    }
}
