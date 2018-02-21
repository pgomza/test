package com.horeca.site.security;

import com.horeca.site.handlers.CustomOAuth2ExceptionRenderer;
import com.horeca.site.security.models.GuestAccount;
import com.horeca.site.security.models.RootAccount;
import com.horeca.site.security.models.SalesmanAccount;
import com.horeca.site.security.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private OAuth2WebSecurityExpressionHandler expressionHandler;

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

        // set the declared expression handler explicitly (otherwise its bean resolver is null)
        resources.expressionHandler(expressionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /*
        *******************************************************************************
        *******************************************************************************
            NO AUTHENTICATION REQUIRED FOR THE FOLLOWING ENDPOINTS
        *******************************************************************************
        *******************************************************************************
         */

        http.authorizeRequests().antMatchers("/api/updates/**").permitAll();
        http.authorizeRequests().antMatchers("/api/demo/**").permitAll();
        http.authorizeRequests().antMatchers("/api/timeout").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/languages").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/currencies").permitAll();
        // the logging endpoints are secured separately
        http.authorizeRequests().antMatchers("/api/logs/**").permitAll();

        // allow everybody but anons to access static translations
        http.authorizeRequests().antMatchers("/api/static-translations/**")
                .hasAnyRole(UserAccount.ROLE_HOTEL_FULL, GuestAccount.ROLE_DEFAULT);

        // allow anybody to create a user account (but only with a new hotel)
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/accounts/users-new-hotel").permitAll();
        // allow anybody to activate their account
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/accounts/users/activation").permitAll();
        // allow anybody to reset their password
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/accounts/users/reset-request").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/accounts/users/reset-confirmation").permitAll();

        // allow anybody to activate their account
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/accounts/salesmen/activation").permitAll();

        // allow anybody to get info about any of the hotels (but not their guests)
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels/{\\d+}").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels/{\\d+}/services/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels/{\\d+}/images/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels/{\\d+}/tv-channels").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/hotels/{\\d+}/links").permitAll();


        /*
        *******************************************************************************
        *******************************************************************************
            AUTHORIZATION BASED ON METHODS DEFINED IN A DESIGNATED BEAN
        *******************************************************************************
        *******************************************************************************
         */

        http.authorizeRequests().antMatchers("/api/hotels/{\\d+}/**")
                .access("@accessChecker.checkForHotel(authentication, request)");

        http.authorizeRequests().antMatchers("/api/stays/{pin}/**")
                .access("@accessChecker.checkForStay(authentication, request)");
        http.authorizeRequests().antMatchers("/api/check-in/{pin}")
                .access("@accessChecker.checkForStayCheckIn(authentication, request)");
        http.authorizeRequests().antMatchers("/api/check-out/{pin}")
                .access("@accessChecker.checkForStayCheckOut(authentication, request)");

        // each salesman has access to their data
        http.authorizeRequests().antMatchers("/api/accounts/salesmen/{login}/**")
                .access("@accessChecker.checkForSalesman(authentication, request)");

        // each user has access to their data
        http.authorizeRequests().antMatchers("/api/accounts/users/{login}/**")
                .access("@accessChecker.checkForUser(authentication, request)");

        /*
        *******************************************************************************
        *******************************************************************************
            AUTHORIZATION BASED ON ROLES THAT HAVE BEEN GRANTED
        *******************************************************************************
        *******************************************************************************
         */

        // only the ones that have the 'SALESMAN' role can add hotels through the standard endpoint
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/hotels").hasAuthority(SalesmanAccount.ROLE_DEFAULT);

        // only roots can manage all salesmen
        http.authorizeRequests().antMatchers("/api/accounts/salesmen/**").hasAuthority(RootAccount.ROLE_DEFAULT);

        // only accounts with the 'SALESMAN' role can manage all the users
        http.authorizeRequests().antMatchers("/api/accounts/users/**").hasAuthority(SalesmanAccount.ROLE_DEFAULT);

        /*
        *******************************************************************************
        *******************************************************************************
            ANYONE THAT HAS BEEN AUTHENTICATED CAN ACCESS THE REST OF THE ENDPOINTS
        *******************************************************************************
        *******************************************************************************
         */

        // make sure that the rest of the endpoints is properly secured
        http.authorizeRequests().antMatchers("/api/**").authenticated();
    }

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }
}
