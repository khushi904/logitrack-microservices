package org.logitrack.shipmentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(System.getenv().getOrDefault("NOTIFICATION_SERVICE_URL", "http://localhost:8083"))
                .build();
    }
}