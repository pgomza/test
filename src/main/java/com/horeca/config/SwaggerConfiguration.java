package com.horeca.config;

import com.google.common.base.Predicate;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket customEndpoints() {
        List<Parameter> globalParameters = new ArrayList<>();
        Parameter tokenAuthParameter = new ParameterBuilder()
                .name("Authorization")
                .description("access token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();
        globalParameters.add(tokenAuthParameter);

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
                .directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .globalOperationParameters(globalParameters)
                .pathMapping("/")
                .apiInfo(metadata());
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("THRODI's backend API")
                .build();
    }
}
