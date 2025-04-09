package ru.practicum.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.model.Event;
import ru.practicum.service.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiator(User user, Pageable pageable);

    Optional<Event> findByIdAndInitiator(Long id, User user);
}
