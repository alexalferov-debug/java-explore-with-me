package ru.practicum.service.dao.category;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.model.Category;

import java.util.List;

public interface CategoryDao {

    Category save(Category category);

    void delete(Long id);

    Category findById(Long id);

    List<Category> findAll(Pageable pageable);
}
