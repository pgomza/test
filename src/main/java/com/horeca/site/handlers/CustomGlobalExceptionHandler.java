package com.horeca.site.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.horeca.site.exceptions.BadAuthenticationRequestException;
import com.horeca.site.exceptions.BusinessRuleViolationException;
import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.exceptions.UnauthorizedException;
import com.horeca.site.security.models.AbstractAccount;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class CustomGlobalExceptionHandler extends AbstractHandlerExceptionResolver {

    private static final Logger logger = Logger.getLogger(CustomGlobalExceptionHandler.class.getName());

    private final List<Class<? extends Exception>> BAD_REQUEST_EXCEPTIONS = new ArrayList<>();
    private final List<Class<? extends Exception>> NOT_FOUND_EXCEPTIONS = new ArrayList<>();
    private final List<Class<? extends Exception>> UNAUTHORIZED_EXCEPTIONS = new ArrayList<>();
    private final List<Class<? extends Exception>> FORBIDDEN_EXCEPTIONS = new ArrayList<>();

    public CustomGlobalExceptionHandler() {
        BAD_REQUEST_EXCEPTIONS.add(BusinessRuleViolationException.class);
        BAD_REQUEST_EXCEPTIONS.add(ConstraintViolationException.class);
        BAD_REQUEST_EXCEPTIONS.add(DataIntegrityViolationException.class);
        BAD_REQUEST_EXCEPTIONS.add(InvalidDataAccessApiUsageException.class);
        BAD_REQUEST_EXCEPTIONS.add(HttpRequestMethodNotSupportedException.class);
        BAD_REQUEST_EXCEPTIONS.add(HttpMediaTypeNotSupportedException.class);
        BAD_REQUEST_EXCEPTIONS.add(HttpMessageNotReadableException.class);
        BAD_REQUEST_EXCEPTIONS.add(HttpMessageNotWritableException.class);
        BAD_REQUEST_EXCEPTIONS.add(MultipartException.class);
        BAD_REQUEST_EXCEPTIONS.add(JsonMappingException.class);
        BAD_REQUEST_EXCEPTIONS.add(MissingServletRequestParameterException.class);
        BAD_REQUEST_EXCEPTIONS.add(UnsatisfiedServletRequestParameterException.class);
        BAD_REQUEST_EXCEPTIONS.add(ServletRequestBindingException.class);
        BAD_REQUEST_EXCEPTIONS.add(BindException.class);
        BAD_REQUEST_EXCEPTIONS.add(TypeMismatchException.class);
        BAD_REQUEST_EXCEPTIONS.add(MethodArgumentNotValidException.class);
        BAD_REQUEST_EXCEPTIONS.add(BadAuthenticationRequestException.class);
        BAD_REQUEST_EXCEPTIONS.add(UnsupportedGrantTypeException.class);
        BAD_REQUEST_EXCEPTIONS.add(DisabledException.class);
        BAD_REQUEST_EXCEPTIONS.add(IllegalArgumentException.class);

        NOT_FOUND_EXCEPTIONS.add(ResourceNotFoundException.class);

        UNAUTHORIZED_EXCEPTIONS.add(BadCredentialsException.class);
        UNAUTHORIZED_EXCEPTIONS.add(UnauthorizedException.class);

        FORBIDDEN_EXCEPTIONS.add(AccessDeniedException.class);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String message = ex.getMessage();
        String timestamp = new DateTime().toString();

        logger.warn("Message: " + ex.getMessage());
        logger.warn("URI: " + request.getRequestURI());
        if (handler != null) { // not sure if that can ever be false
            logger.warn("Handler: " + handler);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AbstractAccount) {
            logger.warn("Principal: " + ((AbstractAccount) authentication.getPrincipal()).getUsername());
        }
        else if (authentication instanceof AnonymousAuthenticationToken) {
            logger.warn("Principal: anonymous");
        }

        int statusCode;

        if (checkIfBelongsToList(ex.getClass(), BAD_REQUEST_EXCEPTIONS)) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        else if (checkIfBelongsToList(ex.getClass(), NOT_FOUND_EXCEPTIONS)) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
        }
        else if (checkIfBelongsToList(ex.getClass(), UNAUTHORIZED_EXCEPTIONS)) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
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
