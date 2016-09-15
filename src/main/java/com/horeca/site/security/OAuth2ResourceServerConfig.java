package com.horeca.site.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
        resources.resourceId("AppResources");

        // add a custom entry point to handle oauth exceptions the same way
        // the rest of the exceptions are handled (rendered as a custom json message)
        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setRealmName("AppResources");
        entryPoint.setTypeName("Bearer");
        resources.authenticationEntryPoint(entryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/restricted/**").authenticated();
        http.authorizeRequests().antMatchers("/api/stays/{\\d+}").authenticated();
        http.authorizeRequests().antMatchers("/api/check-in/{\\d+}").authenticated();
        http.authorizeRequests().antMatchers("/api/check-out/{\\d+}").authenticated();
    }
}
