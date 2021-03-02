package com.borchowiec.user.configuration;

import com.borchowiec.remote.client.AuthClient;
import com.borchowiec.remote.client.NotificationClient;
import com.borchowiec.remote.client.UserRepositoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EntityScan("com.borchowiec.user.model")
public class ApplicationConfiguration {
    @Value("${GATEWAY_ADDRESS}")
    private String gatewayUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.create(gatewayUrl);
    }

    @Bean
    public UserRepositoryClient userRepositoryClient() {
        return new UserRepositoryClient(webClient());
    }

    @Bean
    public NotificationClient notificationClient() {
        return new NotificationClient(webClient());
    }

    @Bean
    public AuthClient authClient() {
        return new AuthClient(webClient());
    }
}

