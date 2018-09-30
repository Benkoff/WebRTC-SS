package io.github.benkoff.webrtcss.config;

import io.github.benkoff.webrtcss.socket.SignalHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalingHandler(), "/signal")
                .setAllowedOrigins("*"); // allow all origins
    }

    @Bean
    public WebSocketHandler signalingHandler() {
        return new SignalHandler();
    }
}