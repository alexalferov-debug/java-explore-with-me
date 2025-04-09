package ru.practicum.service.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dao.category.CategoryDao;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.data.EventState;
import ru.practicum.service.data.EventStateAction;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.NewEventDto;
import ru.practicum.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.dto.event.UpdateEventUserRequest;
import ru.practicum.service.exception.EventValidationException;
import ru.practicum.service.mapper.EventMapper;
import ru.practicum.service.model.Category;
import ru.practicum.service.model.Event;
import ru.practicum.service.model.Location;
import ru.practicum.service.model.User;
import ru.practicum.service.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EventsServiceImpl implements EventsService {
    EventDao eventRepository;
    LocationRepository locationRepository;
    UserDao userDao;
    CategoryDao categoryDao;

    @Autowired
    public EventsServiceImpl(EventDao eventRepository,
                             LocationRepository locationRepository,
                             UserDao userRepository,
                             CategoryDao categoryDao) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.userDao = userRepository;
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Event event = EventMapper.INSTANCE.fromDto(newEventDto);
        Location location = locationRepository.insertOrUpdateLocation(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon());
        event.setInitiator(userDao.get(userId));
        event.setCreatedOn(LocalDateTime.now());
        event.setLocation(location);
        event.setCategory(categoryDao.findById(newEventDto.getCategory()));
        Event savedEvent = eventRepository.createEvent(event);
        return EventMapper.INSTANCE.toFullDto(savedEvent);
    }

    @Override
    @Transactional
    public EventFullDto patchCurUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userDao.get(userId);
        Event event = eventRepository.findByIdAndInitiator(eventId, user);
        boolean isChanged = false;
        if (!(event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING))) {
            throw new EventValidationException("Редактировать можно только отмененное или ожидающее модерации событие");
        }
        if (Objects.nonNull(updateEventUserRequest.getAnnotation()) && !event.getAnnotation().equals(updateEventUserRequest.getAnnotation())) {
            isChanged = true;
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (Objects.nonNull(updateEventUserRequest.getCategory()) && !event.getCategory().getId().equals(updateEventUserRequest.getCategory())) {
            isChanged = true;
            Category category = categoryDao.findById(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }
        if (Objects.nonNull(updateEventUserRequest.getDescription()) && !event.getDescription().equals(updateEventUserRequest.getDescription())) {
            isChanged = true;
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (Objects.nonNull(updateEventUserRequest.getEventDate()) && !event.getEventDate().equals(updateEventUserRequest.getEventDate())) {
            isChanged = true;
            event.setEventDate(updateEventUserRequest.getEventDate());
        }
        if (Objects.nonNull(updateEventUserRequest.getLocation()) && !(event.getLocation().getLatitude().equals(updateEventUserRequest.getLocation().getLatitude()) && event.getLocation().getLongitude().equals(updateEventUserRequest.getLocation().getLongitude()))) {
            isChanged = true;
            Location location = locationRepository.insertOrUpdateLocation(updateEventUserRequest.getLocation().getLatitude(), updateEventUserRequest.getLocation().getLongitude());
            event.setLocation(location);
        }
        if (Objects.nonNull(updateEventUserRequest.getPaid()) && !event.getPaid().equals(updateEventUserRequest.getPaid())) {
            isChanged = true;
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (Objects.nonNull(updateEventUserRequest.getParticipantLimit()) && !event.getParticipantLimit().equals(updateEventUserRequest.getParticipantLimit())) {
            isChanged = true;
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (Objects.nonNull(updateEventUserRequest.getRequestModeration()) && !event.getRequestModeration().equals(updateEventUserRequest.getRequestModeration())) {
            isChanged = true;
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (Objects.nonNull(updateEventUserRequest.getTitle()) && !event.getTitle().equals(updateEventUserRequest.getTitle())) {
            isChanged = true;
            event.setTitle(updateEventUserRequest.getTitle());
        }
        if (Objects.nonNull(updateEventUserRequest.getStateAction()) && (updateEventUserRequest.getStateAction().equals(EventStateAction.CANCEL_REVIEW) && !event.getState().equals(EventState.CANCELED))) {
            isChanged = true;
            event.setState(EventState.CANCELED);
        }
        if (isChanged) {
            return EventMapper.INSTANCE.toFullDto(eventRepository.createEvent(event));
        } else {
            return EventMapper.INSTANCE.toFullDto(event);
        }
    }

    @Override
    @Transactional
    public EventFullDto patchAdminEvent(UpdateEventAdminRequest updateEventUserRequest) {
        return null;
    }

    @Override
    public List<EventFullDto> getCurUserEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        User user = userDao.get(userId);
        return eventRepository.findByInitiator(user, pageable)
                .stream()
                .map(EventMapper.INSTANCE::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getCurUserEventById(Long userId, Long eventId) {
        User user = userDao.get(userId);
        return EventMapper.INSTANCE.toFullDto(eventRepository.findByIdAndInitiator(eventId, user));
    }

    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return List.of();
    }
}
