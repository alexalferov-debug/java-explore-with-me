package ru.practicum.service.controller.events;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.data.EventState;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.service.events.EventsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
public class AdminEventsController {
    EventsService eventsService;

    @Autowired
    public AdminEventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(@RequestParam List<Long> usersId,
                                                        @RequestParam List<EventState> states,
                                                        @RequestParam List<Long> categories,
                                                        @RequestParam LocalDateTime rangeStart,
                                                        @RequestParam LocalDateTime rangeEnd,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(eventsService.getEventsForAdmin(usersId, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patch(@RequestBody @Validated UpdateEventAdminRequest event,
                                              @PathVariable @PositiveOrZero Long eventId) {
        return ResponseEntity.ok(eventsService.patchAdminEvent(eventId, event));
    }
}
