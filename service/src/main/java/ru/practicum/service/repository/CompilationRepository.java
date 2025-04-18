package ru.practicum.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.service.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT c " +
            "FROM Compilation c " +
            "WHERE (:pinned is NULL or c.pinned = :pinned)")
    List<Compilation> findAllWithPinned(@Param("pinned") Boolean pinned,
                                        Pageable pageable);
}
