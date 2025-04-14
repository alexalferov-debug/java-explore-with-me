package ru.practicum.service.service.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.repository.EventViewRepository;

@Service
@Transactional(readOnly = true)
public class EventViewService {
    EventViewRepository eventViewRepository;

    @Autowired
    public EventViewService(EventViewRepository eventViewRepository) {
        this.eventViewRepository = eventViewRepository;
    }

    @Transactional
    public void addView(Long eventId, String ipAddress) {
        eventViewRepository.addUniqueView(eventId, ipAddress);
    }

    public Long getUniqueViewCount(Long eventId) {
        return eventViewRepository.findCountByEventId(eventId);
    }

}
