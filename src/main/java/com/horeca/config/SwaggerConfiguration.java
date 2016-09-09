package com.horeca.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket customEndpoints() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("customEndpoints")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(new Predicate<String>() {
                    @Override
                    public boolean apply(String path) {
                        return path.startsWith("/api");
                    }
                })
                .build()
                .pathMapping("/")
                .apiInfo(metadata());
    }

    @Bean
    public Docket oauthEndpoints() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("oauthEndpoints")
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(FrameworkEndpoint.class))
                .paths(new Predicate<String>() {
                    @Override
                    public boolean apply(String path) {
                        return path.startsWith("/oauth/token");
                    }
                })
                .build()
                .pathMapping("/")
                .apiInfo(metadata());
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfiguration.DEFAULT;
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Horeca Club's backend API")
                .build();
    }
}
