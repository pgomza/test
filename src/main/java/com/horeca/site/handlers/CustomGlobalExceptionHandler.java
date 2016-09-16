package com.horeca.site.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.horeca.site.exceptions.BadAuthorizationRequestException;
import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

//TODO disable DefaultHandlerExceptionResolver
@Component
public class CustomGlobalExceptionHandler extends AbstractHandlerExceptionResolver {

    private final List<Class<? extends Exception>> BAD_REQUEST_EXCEPTIONS = new ArrayList<>();
    private final List<Class<? extends Exception>> NOT_FOUND_EXCEPTIONS = new ArrayList<>();
//    private final List<Class<? extends Exception>> UNAUTHORIZED_EXCEPTIONS = new ArrayList<>();
    private final List<Class<? extends Exception>> FORBIDDEN_EXCEPTIONS = new ArrayList<>();

    public CustomGlobalExceptionHandler() {
        BAD_REQUEST_EXCEPTIONS.add(BusinessRuleViolationException.class);
        BAD_REQUEST_EXCEPTIONS.add(MethodArgumentNotValidException.class);
        BAD_REQUEST_EXCEPTIONS.add(HttpMessageNotReadableException.class);
        BAD_REQUEST_EXCEPTIONS.add(BadAuthorizationRequestException.class);
        BAD_REQUEST_EXCEPTIONS.add(JsonMappingException.class);
        BAD_REQUEST_EXCEPTIONS.add(ConstraintViolationException.class);

        NOT_FOUND_EXCEPTIONS.add(ResourceNotFoundException.class);

        FORBIDDEN_EXCEPTIONS.add(AccessDeniedException.class);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String message = ex.getMessage();
//        String timestamp = new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date());
        String timestamp = new DateTime().toString();
        int statusCode = 0;

        if (checkIfBelongsToList(ex.getClass(), BAD_REQUEST_EXCEPTIONS)) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        else if (checkIfBelongsToList(ex.getClass(), NOT_FOUND_EXCEPTIONS)) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
        }
        else if (checkIfBelongsToList(ex.getClass(), FORBIDDEN_EXCEPTIONS)) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
        }
        else {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        response.setStatus(statusCode);
        mav.addObject("message", message);
        mav.addObject("timestamp", timestamp);
        return mav;
    }

    private boolean checkIfBelongsToList(Class<? extends Exception> exceptionInQuestion, List<Class<? extends Exception>> exceptions) {
        for (Class<? extends Exception> ex : exceptions) {
            if (exceptionInQuestion.isAssignableFrom(ex))
                return true;
        }
        return false;
    }
}
