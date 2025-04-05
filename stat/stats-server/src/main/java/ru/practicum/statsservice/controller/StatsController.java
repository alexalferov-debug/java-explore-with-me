package ru.practicum.statsservice.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statscommon.dto.EndpointHit;
import ru.practicum.statscommon.dto.ViewStats;
import ru.practicum.statsservice.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class StatsController {
    StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<Void> hit(@RequestBody EndpointHit endpointHit) {
        statsService.saveHit(endpointHit);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull LocalDateTime end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        List<ViewStats> stats = statsService.getStats(start, end, uris, unique);
        return ResponseEntity.ok(stats);
    }
}
