package ru.practicum.service.controller.requests;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.request.ParticipationRequestDto;
import ru.practicum.service.service.request.RequestsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestsController {
    RequestsService requestsService;

    @Autowired
    public RequestsController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addRequest(@PathVariable @PositiveOrZero Long userId,
                                                              @RequestParam @PositiveOrZero Long eventId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(requestsService.addRequest(userId, eventId, LocalDateTime.now()));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable @PositiveOrZero Long userId,
                                                                 @PathVariable @PositiveOrZero Long requestId) {
        return ResponseEntity.ok(requestsService.cancelRequest(userId, requestId));
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable @PositiveOrZero Long userId) {
        return ResponseEntity.ok(requestsService.getRequestsForCurUser(userId));
    }
}
