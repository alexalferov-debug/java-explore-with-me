package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
