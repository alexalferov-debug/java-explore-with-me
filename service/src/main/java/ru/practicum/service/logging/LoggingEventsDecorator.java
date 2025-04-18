package ru.practicum.service.logging;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.service.data.EventSortParam;
import ru.practicum.service.data.EventState;
import ru.practicum.service.dto.event.*;
import ru.practicum.service.dto.request.ParticipationRequestDto;
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
    public EventFullDto getEventById(Long eventId) {
        log.info("Запрошена информация о событии с id = {}", eventId);
        return delegate.getEventById(eventId);
    }

    @Override
    public EventFullDto getEventByIdAndState(Long eventId, EventState state, String ipAddress) {
        log.info("Запрошена информация о событии id = {} в статусе {} c ip = {}", eventId, state, ipAddress);
        return delegate.getEventByIdAndState(eventId, state, ipAddress);
    }

    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Запрошена информация о событиях администратором с фильтрами: users = {}, states = {} , categories = {}, rangeStart = {}, rangeEnd = {}, from = {}, size = {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return delegate.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortParam sort, Integer from, Integer size) {
        log.info("Запрошена информация о списке событий по публичному эндпоинту с параметрами: text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return delegate.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipation(Long userId, Long eventId, EventRequestStatusUpdateRequest participationRequestDto) {
        log.info("Запрошено изменения статуса на {} для пользователей {} в событии {}  ", participationRequestDto.getStatus(), eventId, participationRequestDto.getRequestIds());
        return delegate.updateParticipation(userId, eventId, participationRequestDto);
    }

    @Override
    public List<ParticipationRequestDto> getParticipation(Long userId, Long eventId) {
        log.info("Запрошен список запросов на участие для события {} ", eventId);
        return delegate.getParticipation(userId, eventId);
    }
}
