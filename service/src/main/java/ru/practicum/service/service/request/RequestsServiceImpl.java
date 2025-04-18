package ru.practicum.service.service.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.data.EventState;
import ru.practicum.service.data.RequestStatus;
import ru.practicum.service.dto.request.ParticipationRequestDto;
import ru.practicum.service.exception.EventValidationException;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.mapper.RequestMapper;
import ru.practicum.service.model.Event;
import ru.practicum.service.model.Request;
import ru.practicum.service.model.User;
import ru.practicum.service.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RequestsServiceImpl implements RequestsService {
    RequestRepository requestRepository;
    UserDao userDao;
    EventDao eventDao;

    @Autowired
    public RequestsServiceImpl(RequestRepository requestRepository,
                               UserDao userDao,
                               EventDao eventDao
    ) {
        this.requestRepository = requestRepository;
        this.userDao = userDao;
        this.eventDao = eventDao;
    }

    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId, LocalDateTime requestTime) {
        User user = userDao.get(userId);
        Event event = eventDao.findById(eventId);
        if (Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new EventValidationException("Нельзя добавить запрос на участие в собственном событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventValidationException("Можно добавить запрос на участие только для опубликованных событий");
        }
        if (event.getParticipantLimit() != 0) {
            Long requestCount = requestRepository.findCountOfConfirmedRequest(event.getId());
            if (requestCount >= event.getParticipantLimit()) {
                throw new EventValidationException("Достигнут лимит участников для события");
            }
        }
        RequestStatus status = RequestStatus.PENDING;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        }
        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(requestTime)
                .status(status)
                .build();
        return RequestMapper.INSTANCE.toDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForCurUser(Long userId) {
        userDao.get(userId);
        return requestRepository.findByRequesterId(userId).stream().map(RequestMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос на участие с id = " + requestId + " не найден"));
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.INSTANCE.toDto(requestRepository.save(request));
    }
}
