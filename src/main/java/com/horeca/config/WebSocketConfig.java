package com.horeca.config;

import com.horeca.site.websocket.WebSocketUpdatesHandler;
import com.horeca.site.websocket.WebSocketUpdatesInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketUpdatesInterceptor interceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(updatesHandler(), "/api/updates")
                .addInterceptors(interceptor)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler updatesHandler() {
        return new WebSocketUpdatesHandler();
    }
}

