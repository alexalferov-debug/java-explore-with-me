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
        return checkPatchRequestChanges(event,
                updateEventUserRequest.getAnnotation(),
                updateEventUserRequest.getCategory(),
                updateEventUserRequest.getDescription(),
                updateEventUserRequest.getEventDate(),
                updateEventUserRequest.getLocation(),
                updateEventUserRequest.getPaid(),
                updateEventUserRequest.getParticipantLimit(),
                updateEventUserRequest.getRequestModeration(),
                updateEventUserRequest.getTitle(),
                updateEventUserRequest.getStateAction(),
                false);
    }

    @Override
    @Transactional
    public EventFullDto patchAdminEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId);
        return checkPatchRequestChanges(event,
                updateEventAdminRequest.getAnnotation(),
                updateEventAdminRequest.getCategory(),
                updateEventAdminRequest.getDescription(),
                updateEventAdminRequest.getEventDate(),
                updateEventAdminRequest.getLocation(),
                updateEventAdminRequest.getPaid(),
                updateEventAdminRequest.getParticipantLimit(),
                updateEventAdminRequest.getRequestModeration(),
                updateEventAdminRequest.getTitle(),
                updateEventAdminRequest.getStateAction(),
                true);
    }

    private EventFullDto checkPatchRequestChanges(Event event,
                                                  String annotation,
                                                  Long categoryRequest,
                                                  String description,
                                                  LocalDateTime eventDate,
                                                  ru.practicum.service.dto.location.Location locationRequest,
                                                  Boolean paid,
                                                  Integer participantLimit,
                                                  Boolean requestModeration,
                                                  String title,
                                                  EventStateAction stateAction,
                                                  boolean isAdmin) {
        boolean isChanged = false;
        if (!isAdmin && !(event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING))) {
            throw new EventValidationException("Редактировать можно только отмененное или ожидающее модерации событие");
        }
        if (Objects.nonNull(annotation) && !event.getAnnotation().equals(annotation)) {
            isChanged = true;
            event.setAnnotation(annotation);
        }
        if (Objects.nonNull(categoryRequest) && !event.getCategory().getId().equals(categoryRequest)) {
            isChanged = true;
            Category category = categoryDao.findById(categoryRequest);
            event.setCategory(category);
        }
        if (Objects.nonNull(description) && !event.getDescription().equals(description)) {
            isChanged = true;
            event.setDescription(description);
        }
        if (Objects.nonNull(eventDate) && !event.getEventDate().equals(eventDate)) {
            isChanged = true;
            event.setEventDate(eventDate);
        }
        if (Objects.nonNull(locationRequest) && !(event.getLocation().getLatitude().equals(locationRequest.getLat()) && event.getLocation().getLongitude().equals(locationRequest.getLon()))) {
            isChanged = true;
            Location location = locationRepository.insertOrUpdateLocation(locationRequest.getLat(), locationRequest.getLon());
            event.setLocation(location);
        }
        if (Objects.nonNull(paid) && !event.getPaid().equals(paid)) {
            isChanged = true;
            event.setPaid(paid);
        }
        if (Objects.nonNull(participantLimit) && !event.getParticipantLimit().equals(participantLimit)) {
            isChanged = true;
            event.setParticipantLimit(participantLimit);
        }
        if (Objects.nonNull(requestModeration) && !event.getRequestModeration().equals(requestModeration)) {
            isChanged = true;
            event.setRequestModeration(requestModeration);
        }
        if (Objects.nonNull(title) && !event.getTitle().equals(title)) {
            isChanged = true;
            event.setTitle(title);
        }
        if (!isAdmin) {
            if (Objects.nonNull(stateAction) && (stateAction.equals(EventStateAction.CANCEL_REVIEW) && !event.getState().equals(EventState.CANCELED))) {
                isChanged = true;
                event.setState(EventState.CANCELED);
            }
        } else {
            EventState state = EventStateAction.getByAction(stateAction);
            if (Objects.nonNull(state) && !event.getState().equals(state)) {
                isChanged = true;
                event.setState(state);
            }
        }
        if (isChanged) {
            return EventMapper.INSTANCE.toFullDto(eventRepository.createEvent(event));
        } else {
            return EventMapper.INSTANCE.toFullDto(event);
        }
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
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.getRequestsForAdminWithFiltering(users, states, categories, rangeStart, rangeEnd, pageable).stream().map(EventMapper.INSTANCE::toFullDto).collect(Collectors.toList());
    }
}
