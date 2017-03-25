package com.horeca.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PushStateResourceResolver());
    }

    private class PushStateResourceResolver implements ResourceResolver {
        private Resource index = new ClassPathResource("/static/index.html");
        private List<String> handledExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "woff", "appcache", "jpg", "jpeg", "gif", "ico");
        private List<String> ignoredPaths = Arrays.asList("api", "swagger-ui.html");
        private List<Resource> swaggerResources = Arrays.asList(new Resource[] { new ClassPathResource("/META-INF/resources/") });

        @Override
        public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resource = resolve(requestPath, locations);
            if (resource == null) {
                return chain.resolveResource(request, requestPath, swaggerResources);
            }
            return resource;
        }

        @Override
        public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resolvedResource = resolve(resourcePath, locations);
            if (resolvedResource == null) {
                return chain.resolveUrlPath(resourcePath, swaggerResources);
            }
            try {
                return resolvedResource.getURL().toString();
            } catch (IOException e) {
                return resolvedResource.getFilename();
            }
        }

        private Resource resolve(String requestPath, List<? extends Resource> locations) {
            if (isIgnored(requestPath)) {
                return null;
            }
            if (isHandled(requestPath)) {
                for (Resource location : locations) {
                    Resource relative = createRelative(location, requestPath);
                    if (relative != null && relative.exists())
                        return relative;
                    else
                        return null;
                }
            }
            return index;
        }

        private Resource createRelative(Resource resource, String relativePath) {
            try {
                return resource.createRelative(relativePath);
            } catch (IOException e) {
                return null;
            }
        }

        private boolean isIgnored(String path) {
            return ignoredPaths.contains(path);
        }

        private boolean isHandled(String path) {
            String extension = StringUtils.getFilenameExtension(path);
            for (String handleExtension : handledExtensions) {
                if (handleExtension.equals(extension))
                    return true;
            }
            return false;
        }
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
        configurer.favorPathExtension(false);
        configurer.favorParameter(false);
        configurer.ignoreAcceptHeader(false);
        configurer.useJaf(false);
    }
}
