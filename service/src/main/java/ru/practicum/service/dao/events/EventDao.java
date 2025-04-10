package ru.practicum.service.dao.events;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.data.EventState;
import ru.practicum.service.model.Event;
import ru.practicum.service.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDao {
    Event createEvent(Event event);

    List<Event> findByInitiator(User user, Pageable pageable);

    Event findByIdAndInitiator(Long eventId, User user);

    Event findById(Long eventId);

    List<Event> getRequestsForAdminWithFiltering(List<Long> users,
                                                 List<EventState> states,
                                                 List<Long> categories,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 Pageable pageable);
}