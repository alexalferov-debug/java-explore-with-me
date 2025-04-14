package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.service.model.EventView;

public interface EventViewRepository extends JpaRepository<EventView, Long> {

    @Query("SELECT COUNT(v) " +
            "FROM EventView v " +
            "WHERE v.event.id = :eventId")
    Long findCountByEventId(Long eventId);

    @Modifying
    @Query(value = "INSERT INTO event_views (event_id, ip_address) " +
            "VALUES (:eventId, :ipAddress) " +
            "ON CONFLICT (event_id, ip_address) DO NOTHING",
            nativeQuery = true)
    void addUniqueView(@Param("eventId") Long eventId, @Param("ipAddress") String ipAddress);
}
