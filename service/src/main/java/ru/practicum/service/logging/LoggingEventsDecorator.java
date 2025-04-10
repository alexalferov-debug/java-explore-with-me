package ru.practicum.service.logging;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.service.data.EventState;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.NewEventDto;
import ru.practicum.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.dto.event.UpdateEventUserRequest;
import ru.practicum.service.service.events.EventsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class LoggingEventsDecorator implements EventsService {
    EventsService delegate;

    public LoggingEventsDecorator(EventsService delegate) {
        this.delegate = delegate;
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        log.info("Создание события: {}", newEventDto);
        return delegate.createEvent(userId, newEventDto);
    }

    @Override
    public EventFullDto patchCurUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.info("Модификация события пользователем: {}", updateEventUserRequest);
        return delegate.patchCurUserEvent(userId, eventId, updateEventUserRequest);
    }

    @Override
    public EventFullDto patchAdminEvent(Long eventId, UpdateEventAdminRequest updateEventUserRequest) {
        log.info("Модификация события администратором: {}", updateEventUserRequest);
        return delegate.patchAdminEvent(eventId, updateEventUserRequest);
    }

    @Override
    public List<EventFullDto> getCurUserEvents(Long userId, Integer from, Integer size) {
        log.info("Запрошен список событий пользователем {} с даты {} количество событий {}", userId, from, size);
        return delegate.getCurUserEvents(userId, from, size);
    }

    @Override
    public EventFullDto getCurUserEventById(Long userId, Long eventId) {
        log.info("Запрошена информация о событии с id = {} пользователем с id = {}", eventId, userId);
        return delegate.getCurUserEventById(userId, eventId);
    }

    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Запрошена информация о событиях администратором с фильтрами: users = {}, states = {} , categories = {}, rangeStart = {}, rangeEnd = {}, from = {}, size = {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return delegate.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
