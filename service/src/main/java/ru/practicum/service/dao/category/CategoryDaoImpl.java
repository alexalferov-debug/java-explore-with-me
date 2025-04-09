package ru.practicum.service.dao.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.model.Category;
import ru.practicum.service.repository.CategoryRepository;

import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {
    CategoryRepository categoryRepository;

    @Autowired
    public CategoryDaoImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Category with id %s not found", id)));
    }

    @Override
    public List<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).getContent();
    }

}
