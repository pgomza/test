package com.horeca.site.security;

import com.horeca.site.handlers.CustomOAuth2ExceptionRenderer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("throdiResources");

        // add a custom entry point to handle oauth exceptions the same way
        // the rest of the exceptions are handled (rendered as a custom json message)
        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setRealmName("throdiResources");
        entryPoint.setTypeName("Bearer");
        entryPoint.setExceptionRenderer(new CustomOAuth2ExceptionRenderer());
        resources.authenticationEntryPoint(entryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels/**").permitAll();
        http.authorizeRequests().antMatchers("/api/**").authenticated();
    }
}
