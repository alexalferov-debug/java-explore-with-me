package ru.practicum.service.service.events;

import ru.practicum.service.data.EventState;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.NewEventDto;
import ru.practicum.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.dto.event.UpdateEventUserRequest;

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

    List<EventFullDto> getEventsForAdmin(List<Long> users,
                                         List<EventState> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Integer from,
                                         Integer size);
}
