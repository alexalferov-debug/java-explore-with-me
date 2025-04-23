package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.model.ReportReasons;

public interface ReportReasonRepository extends JpaRepository<ReportReasons, Long> {
}
