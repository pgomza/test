package com.horeca.site.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${oauth2.mobile.clientId}")
    private String mobileClientId;

    @Value("${oauth2.mobile.secret}")
    private String mobileSecret;

    @Value("${oauth2.tokenValidationPeriod}")
    private Integer tokenValidationPeriod;

    @Autowired
    private LoginService loginService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore());
        endpoints.approvalStore(approvalStore());

        CustomTokenGranter customTokenGranter =
                new CustomTokenGranter(manager, endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                        endpoints.getOAuth2RequestFactory(), loginService);
        endpoints.tokenGranter(customTokenGranter);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(mobileClientId)
                .secret(mobileSecret)
                .authorizedGrantTypes("password-like")
                .scopes("read", "write")
                .resourceIds("AppResources")
                .accessTokenValiditySeconds(tokenValidationPeriod);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }
}
