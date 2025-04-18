package ru.practicum.service.dao.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.service.data.EventState;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.model.Event;
import ru.practicum.service.model.User;
import ru.practicum.service.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class EventDaoImpl implements EventDao {
    EventRepository eventRepository;

    @Autowired
    public EventDaoImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> findByInitiator(User user, Pageable pageable) {
        return eventRepository.findByInitiator(user, pageable);
    }

    @Override
    public Event findByIdAndInitiator(Long eventId, User user) {
        return eventRepository.findByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException("Событие = " + eventId + " не найдено для пользователя " + user.getId()));
    }

    @Override
    public Event findById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие = " + eventId + " не найдено"));
    }

    @Override
    public Event findByIdAndState(Long eventId, EventState state) {
        return eventRepository.findByIdAndState(eventId, state).orElseThrow(() -> new NotFoundException("Событие = " + eventId + " не найдено в статусе " + state));
    }

    @Override
    public List<Event> findByIdIn(List<Long> eventIds) {
        return eventRepository.findByIdIn(eventIds);
    }

    @Override
    public List<Event> getEventsForAdminWithFiltering(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        return eventRepository.findEventsForAdminWithFiltering(users, states, categories, rangeStart, rangeEnd, pageable).getContent();
    }

    @Override
    public List<Event> getEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable) {
        if (Objects.isNull(onlyAvailable)) {
            onlyAvailable = false;
        }
        if (Objects.isNull(text)) {
            text = "";
        }
        return eventRepository.findEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable).getContent();
    }

    @Override
    public Long findEventsCountByCategoryId(Long categoryId) {
        return eventRepository.findEventCountByCategoryId(categoryId);
    }
}
