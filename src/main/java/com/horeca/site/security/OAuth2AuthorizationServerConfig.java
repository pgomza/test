package com.horeca.site.security;

import com.horeca.site.security.services.LoginService;
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

    /*
        TODO move these properties to a different structure e.g. to a different class
        with the help of the ConfigurationProperties annotation
     */
    public final static String MOBILE_CLIENT_ID = "throdiMobile";
    @Value("${oauth2.mobile.secret}")
    private String mobileSecret;
    private final static Integer MOBILE_TOKEN_VALIDITY_SECONDS = 900; // 15 minutes

    public final static String PANEL_CLIENT_ID = "throdiPanel";
    @Value("${oauth2.panel.secret}")
    private String panelSecret;
    private final static Integer PANEL_TOKEN_VALIDITY_SECONDS = 2678400; // 31 days

    public final static String SALES_CLIENT_ID = "throdiSales";
    @Value("${oauth2.sales.secret}")
    private String salesSecret;
    private final static Integer SALES_TOKEN_VALIDITY_SECONDS = 3600; // 1 hour

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
                    .withClient(MOBILE_CLIENT_ID)
                    .secret(mobileSecret)
                    .authorizedGrantTypes("password-like")
                    .scopes("read", "write")
                    .resourceIds("throdiResources")
                    .accessTokenValiditySeconds(MOBILE_TOKEN_VALIDITY_SECONDS)
                .and()
                    .withClient(PANEL_CLIENT_ID)
                    .secret(panelSecret)
                    .authorizedGrantTypes("password-like")
                    .scopes("read", "write")
                    .resourceIds("throdiResources")
                    .accessTokenValiditySeconds(PANEL_TOKEN_VALIDITY_SECONDS)
                .and()
                    .withClient(SALES_CLIENT_ID)
                    .secret(salesSecret)
                    .authorizedGrantTypes("password-like")
                    .scopes("read", "write")
                    .resourceIds("throdiResources")
                    .accessTokenValiditySeconds(SALES_TOKEN_VALIDITY_SECONDS);
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
