package ru.practicum.service.controller.events;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.event.*;
import ru.practicum.service.dto.request.ParticipationRequestDto;
import ru.practicum.service.service.events.EventsService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {
    EventsService eventsService;

    @Autowired
    public PrivateEventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PathVariable @PositiveOrZero Long userId, @RequestBody @Validated NewEventDto newEventDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventsService.createEvent(userId, newEventDto));
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> get(@PathVariable @PositiveOrZero Long userId,
                                                  @RequestParam(defaultValue = "0")
                                                  @Min(0)
                                                  int from,
                                                  @RequestParam(defaultValue = "10")
                                                  @Min(1)
                                                  int size) {
        return ResponseEntity
                .ok(eventsService.getCurUserEvents(userId, from, size));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> get(@PathVariable @PositiveOrZero Long userId,
                                            @PathVariable @PositiveOrZero Long eventId) {
        return ResponseEntity.ok(eventsService.getCurUserEventById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable @PositiveOrZero Long userId,
                                                   @PathVariable @PositiveOrZero Long eventId,
                                                   @RequestBody @Validated UpdateEventUserRequest request) {
        return ResponseEntity.ok(eventsService.patchCurUserEvent(userId, eventId, request));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateStatusForRequests(@PathVariable @PositiveOrZero Long userId,
                                                                                  @PathVariable @PositiveOrZero Long eventId,
                                                                                  @RequestBody @Validated EventRequestStatusUpdateRequest request) {

        return ResponseEntity.ok(eventsService.updateParticipation(userId, eventId, request));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> updateStatusForRequests(@PathVariable @PositiveOrZero Long userId,
                                                                                 @PathVariable @PositiveOrZero Long eventId) {

        return ResponseEntity.ok(eventsService.getParticipation(userId, eventId));
    }
}

