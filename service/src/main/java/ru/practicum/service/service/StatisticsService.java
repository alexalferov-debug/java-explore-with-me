package ru.practicum.service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.statsclient.StatsClient;
import ru.practicum.statscommon.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class StatisticsService {

    private final StatsClient statsClient;
    private final String serviceName;

    @Autowired
    public StatisticsService(StatsClient statsClient,
                             @Value("${spring.application.name}") String serviceName) {
        this.statsClient = statsClient;
        this.serviceName = serviceName;
    }

    public void recordHit(HttpServletRequest request) {
        CompletableFuture.runAsync(() -> {
            try {
                EndpointHit hit = buildHit(request);
                statsClient.saveHit(hit);
            } catch (Exception e) {
                System.out.println("Ошибка записи статистики: " + e.getMessage());
            }
        });
    }

    private EndpointHit buildHit(HttpServletRequest request) {
        return EndpointHit.builder()
                .app(serviceName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
    }
}