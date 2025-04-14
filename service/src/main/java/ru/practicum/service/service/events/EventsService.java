package ru.practicum.service.service.events;

import ru.practicum.service.data.EventSortParam;
import ru.practicum.service.data.EventState;
import ru.practicum.service.dto.event.*;
import ru.practicum.service.dto.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto patchCurUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto patchAdminEvent(Long eventId, UpdateEventAdminRequest updateEventUserRequest);

    List<EventFullDto> getCurUserEvents(Long userId,
                                        Integer from,
                                        Integer size);

    EventFullDto getCurUserEventById(Long userId,
                                     Long eventId);

    EventFullDto getEventById(Long eventId);

    EventFullDto getEventByIdAndState(Long eventId, EventState state, String ipAddress);

    List<EventFullDto> getEventsForAdmin(List<Long> users,
                                         List<EventState> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Integer from,
                                         Integer size);

    List<EventShortDto> getEventsPublic(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSortParam sort,
            Integer from,
            Integer size);

    EventRequestStatusUpdateResult updateParticipation(Long userId, Long eventId, EventRequestStatusUpdateRequest participationRequestDto);

    List<ParticipationRequestDto> getParticipation(Long userId, Long eventId);
}
