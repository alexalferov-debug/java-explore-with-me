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

    Event findByIdAndState(Long eventId, EventState state);

    List<Event> findByIdIn(List<Long> eventIds);

    List<Event> getEventsForAdminWithFiltering(List<Long> users,
                                               List<EventState> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Pageable pageable);

    List<Event> getEventsPublic(String text,
                                List<Long> categories,
                                Boolean paid,
                                LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                Boolean onlyAvailable,
                                Pageable pageable);

    Long findEventsCountByCategoryId(Long categoryId);
}