package com.eventbooking.booking_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientService {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
