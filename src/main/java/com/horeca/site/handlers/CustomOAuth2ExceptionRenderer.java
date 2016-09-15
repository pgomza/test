package com.horeca.site.handlers;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.horeca.site.exceptions.ErrorResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;

public class CustomOAuth2ExceptionRenderer extends DefaultOAuth2ExceptionRenderer {

    @Override
    public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
        Object responseBody = responseEntity.getBody();
        if (responseBody instanceof OAuth2Exception && responseEntity instanceof ResponseEntity) {
            OAuth2Exception ex = (OAuth2Exception) responseBody;

            HttpServletResponse servletResponse = (HttpServletResponse) webRequest.getNativeResponse();
            HttpStatus statusCode = ((ResponseEntity<?>) responseEntity).getStatusCode();
            servletResponse.setStatus(statusCode.value());
            servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

            ObjectMapper mapper = new ObjectMapper();
            ErrorResponse body = new ErrorResponse();
            body.setMessage(ex.getMessage());
            body.setTimestamp(new DateTime().toString());

            mapper.writeValue(servletResponse.getWriter(), body);
        }
        else
            super.handleHttpEntityResponse(responseEntity, webRequest);
    }
}
