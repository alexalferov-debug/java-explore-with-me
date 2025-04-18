package ru.practicum.service.service.request;

import ru.practicum.service.dto.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestsService {

    ParticipationRequestDto addRequest(Long userId, Long eventId, LocalDateTime requestTime);

    List<ParticipationRequestDto> getRequestsForCurUser(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
