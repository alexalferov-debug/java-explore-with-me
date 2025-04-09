package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.service.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "INSERT INTO locations (lat, lon) VALUES (:lat, :lon) " +
            "ON CONFLICT (lat, lon) DO UPDATE SET lat = EXCLUDED.lat " +
            "RETURNING *",
            nativeQuery = true)
    Location insertOrUpdateLocation(@Param("lat") Float lat, @Param("lon") Float lon);
}
