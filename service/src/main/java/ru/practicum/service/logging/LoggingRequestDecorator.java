package ru.practicum.service.logging;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.service.dto.request.ParticipationRequestDto;
import ru.practicum.service.service.request.RequestsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class LoggingRequestDecorator implements RequestsService {
    private final RequestsService delegate;

    public LoggingRequestDecorator(RequestsService requestsService) {
        this.delegate = requestsService;
    }

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId, LocalDateTime requestTime) {
        log.info("Получен запрос на добавление запроса на участие: userId = {}, eventId = {}", userId, eventId);
        return delegate.addRequest(userId, eventId, requestTime);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForCurUser(Long userId) {
        log.info("Запрошен список запросов на участие для пользователя с id = {}", userId);
        return delegate.getRequestsForCurUser(userId);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Получен запрос на отмену запроса c id = {} от пользователя {}", requestId, userId);
        return delegate.cancelRequest(userId, requestId);
    }
}
