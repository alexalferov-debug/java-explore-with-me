package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.service.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT COUNT(r) " +
            "FROM Request r " +
            "WHERE r.event.id = :event " +
            "AND r.status = 'CONFIRMED'")
    Long findCountOfConfirmedRequest(@Param("event") Long eventId);

    List<Request> findByEventId(Long eventId);

    List<Request> findByRequesterId(Long requesterId);
}
