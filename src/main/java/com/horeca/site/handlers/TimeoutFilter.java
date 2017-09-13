package com.horeca.site.handlers;

import com.horeca.site.models.TimeoutSettings;
import com.horeca.site.repositories.TimeoutSettingsRepository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class TimeoutFilter implements Filter {

    private static final boolean IS_DEVELOPMENT_ENVIRONMENT;
    private final TimeoutSettingsRepository repository;

    static {
        String activeProfile = System.getenv("spring.profiles.active");
        if (Objects.equals(activeProfile, "production")) {
            IS_DEVELOPMENT_ENVIRONMENT = false;
        }
        else {
            IS_DEVELOPMENT_ENVIRONMENT = true;
        }
    }

    public TimeoutFilter(TimeoutSettingsRepository settingsRepository) {
        repository = settingsRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            String uri = ((HttpServletRequest) request).getRequestURI();
            if (IS_DEVELOPMENT_ENVIRONMENT) {
                if (!Objects.equals(uri, "/api/timeout")) {
                    TimeoutSettings settings = repository.findOne(1L);
                    if (settings.isEnabled()) {
                        try {
                            timeoutRequest();
                        } catch (InterruptedException e) {
                        } finally {
                            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                            return;
                        }
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    private void timeoutRequest() throws InterruptedException {
        Thread.sleep(100_000);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
