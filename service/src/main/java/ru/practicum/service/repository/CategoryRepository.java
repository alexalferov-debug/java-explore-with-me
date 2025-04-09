package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
