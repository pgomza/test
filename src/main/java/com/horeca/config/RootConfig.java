package com.horeca.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.appengine.api.utils.SystemProperty;
import com.horeca.site.handlers.CustomGlobalExceptionHandler;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
public class RootConfig extends WebMvcConfigurerAdapter
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
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        return objectMapper;
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new CustomGlobalExceptionHandler());
    }
}
