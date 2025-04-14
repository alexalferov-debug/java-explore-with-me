package ru.practicum.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.service.data.EventState;
import ru.practicum.service.model.Event;
import ru.practicum.service.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiator(User user, Pageable pageable);

    Optional<Event> findByIdAndInitiator(Long id, User user);

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "(:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND e.eventDate >= COALESCE(:rangeStart, e.eventDate) " +
            "AND e.eventDate <= COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> findEventsForAdminWithFiltering(
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable
    );

    @Query("SELECT COUNT (e) " +
            "FROM Event e " +
            "WHERE e.category.id = :category")
    Long findEventCountByCategoryId(
            @Param("category") Long categoryId
    );

    @Query("SELECT e FROM Event e " +
            "WHERE (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (e.state = 'PUBLISHED') " +
            "AND (:paid is NULL OR e.paid = :paid ) " +
            "AND e.eventDate >= COALESCE(:rangeStart, e.eventDate) " +
            "AND e.eventDate <= COALESCE(:rangeEnd, e.eventDate)" +
            "AND ((:onlyAvailable = true AND e.confirmedRequests < e.participantLimit) OR (:onlyAvailable = false))")
    Page<Event> findEventsPublic(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable
    );
}
