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

    private final static String mobileClientId = "throdiMobile";
    @Value("${oauth2.mobile.secret}")
    private String mobileSecret;
    private final static Integer mobileTokenValiditySeconds = 900; // 15 minutes

    private final static String panelClientId = "throdiPanel";
    @Value("${oauth2.panel.secret}")
    private String panelSecret;
    private final static Integer panelTokenValiditySeconds = 2678400; // 31 days

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
                    .resourceIds("throdiResources")
                    .accessTokenValiditySeconds(mobileTokenValiditySeconds)
                .and()
                    .withClient(panelClientId)
                    .secret(panelSecret)
                    .authorizedGrantTypes("password-like")
                    .scopes("read", "write")
                    .resourceIds("throdiResources")
                    .accessTokenValiditySeconds(panelTokenValiditySeconds);
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
