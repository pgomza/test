package com.horeca.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.horeca.site.handlers.CustomGlobalExceptionHandler;
import com.horeca.site.handlers.UpdatesInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableScheduling
public class RootConfig extends WebMvcConfigurerAdapter
{
    @Autowired
    private UpdatesInterceptor updatesInterceptor;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JodaModule());

        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        objectMapper.registerModule(hibernate5Module);

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(updatesInterceptor);
        registration.addPathPatterns("/api/**");
    }
}
