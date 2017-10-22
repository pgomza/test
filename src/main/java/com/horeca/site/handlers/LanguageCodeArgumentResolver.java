package com.horeca.site.handlers;

import com.horeca.site.models.hotel.translation.LanguageCode;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class LanguageCodeArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LanguageCode.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String language = request.getLocale().getLanguage();

        LanguageCode languageCode = null;
        if (!language.isEmpty()) {
            try {
                languageCode = LanguageCode.valueOf(language.toUpperCase());
            } catch (IllegalArgumentException e) {}
        }

        return languageCode;
    }
}
