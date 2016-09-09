package com.horeca.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.appengine.api.utils.SystemProperty;
import com.google.common.base.Predicate;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.sql.DataSource;

@Configuration
public class RootConfig
{
    @Value("${datasource.driverclassname.gae}")
    private String gaeDataSourceDriverClassName;

    @Value("${datasource.driverclassname.local}")
    private String localDataSourceDriverClassName;

    @Value("${datasource.connectionurl.gae}")
    private String gaeDataSourceConnectionUrl;

    @Value("${datasource.connectionurl.local}")
    private String localDataSourceConnectionUrl;

    @Value("${datasource.username}")
    private String dataSourceUsername;

    @Value("${datasource.password}")
    private String dataSourcePassword;

    //TODO change datasource's implementation to some more efficient one
    @Bean(destroyMethod="close")
    public DataSource basicDataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
            dataSource.setDriverClassName(gaeDataSourceDriverClassName);
            dataSource.setUrl(gaeDataSourceConnectionUrl);
        }
        else {
            dataSource.setDriverClassName(localDataSourceDriverClassName);
            dataSource.setUrl(localDataSourceConnectionUrl);
        }
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);

        return dataSource;
    }

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        StaticMessageSource messageSource = new StaticMessageSource();
        return messageSource;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
