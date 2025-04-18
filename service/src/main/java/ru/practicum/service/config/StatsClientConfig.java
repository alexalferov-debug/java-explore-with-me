package ru.practicum.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsclient.StatsClient;

import java.time.Duration;

@Configuration
public class StatsClientConfig {
    @Value("${stats-server.url}")
    private String statsServerUrl;
    @Value("${stats-server.connectTimeout}")
    private Integer connectTimeout;
    @Value("${stats-server.readTimeout}")
    private Integer readTimeout;


    @Bean
    public RestTemplate statsRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    @Bean
    public StatsClient statsClient(RestTemplate statsRestTemplate) {
        return new StatsClient(statsRestTemplate, statsServerUrl);
    }
}
