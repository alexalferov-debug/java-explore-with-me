package ru.practicum.statsclient;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.statscommon.dto.EndpointHit;
import ru.practicum.statscommon.dto.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatsClient {
    private final RestTemplate restTemplate;
    private final String serverUrl;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(RestTemplate restTemplate, String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public void saveHit(EndpointHit hit) {
        HttpEntity<EndpointHit> request = new HttpEntity<>(hit);
        ResponseEntity<Void> response = restTemplate.postForEntity(serverUrl + "/hit", request, Void.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to save hit: " + response.getStatusCode());
        }
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String startFormatted = URLEncoder.encode(start.format(formatter), StandardCharsets.UTF_8);
        String endFormatted = URLEncoder.encode(end.format(formatter), StandardCharsets.UTF_8);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serverUrl + "/stats")
                .queryParam("start", startFormatted)
                .queryParam("end", endFormatted)
                .queryParam("unique", unique != null ? unique : false);

        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", String.join(",", uris));
        }

        String uri = builder.encode().toUriString();

        ResponseEntity<ViewStats[]> response = restTemplate.getForEntity(uri, ViewStats[].class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return List.of(response.getBody());
        } else {
            throw new RuntimeException("Failed to get stats: " + response.getStatusCode());
        }
    }
}