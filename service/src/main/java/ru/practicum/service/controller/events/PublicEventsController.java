package ru.practicum.service.controller.events;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.data.EventSortParam;
import ru.practicum.service.data.EventState;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.dto.event.comment.ShortCommentDto;
import ru.practicum.service.service.StatisticsService;
import ru.practicum.service.service.events.EventsService;
import ru.practicum.service.service.events.comments.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
public class PublicEventsController {
    private final StatisticsService statisticsService;
    private final EventsService eventsService;
    private final CommentService commentService;

    public PublicEventsController(StatisticsService statisticsService,
                                  EventsService eventsService,
                                  CommentService commentService) {
        this.statisticsService = statisticsService;
        this.eventsService = eventsService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) EventSortParam sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size,
            HttpServletRequest request
    ) {
        List<EventShortDto> result = eventsService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        statisticsService.recordHit(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long id,
                                                 HttpServletRequest request) {
        EventFullDto eventFullDto = eventsService.getEventByIdAndState(id, EventState.PUBLISHED, request.getRemoteAddr());
        statisticsService.recordHit(request);
        return ResponseEntity.ok(eventFullDto);
    }

    @GetMapping("/{eventId}/comments")
    public ResponseEntity<List<ShortCommentDto>> getComments(@PathVariable @PositiveOrZero Long eventId,
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                             @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        return ResponseEntity.ok(commentService.getComments(eventId, from, size));
    }
}
