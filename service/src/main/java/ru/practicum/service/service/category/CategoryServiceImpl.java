package ru.practicum.service.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dao.category.CategoryDao;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;
import ru.practicum.service.exception.EventValidationException;
import ru.practicum.service.mapper.CategoryMapper;
import ru.practicum.service.model.Category;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao;
    EventDao eventDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao, EventDao eventDao) {
        this.categoryDao = categoryDao;
        this.eventDao = eventDao;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.toDto(categoryDao.save(CategoryMapper.INSTANCE.fromDto(categoryDto)));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (eventDao.findEventsCountByCategoryId(id) > 0) {
            throw new EventValidationException("Невозможно удалить категорию с id " + id + ", так как к данной категории привязаны события");
        }
        categoryDao.delete(id);
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(Long id, NewCategoryDto categoryDto) {
        Category category = categoryDao.findById(id);
        category.setName(categoryDto.getName());
        return CategoryMapper.INSTANCE.toDto(categoryDao.save(category));
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return CategoryMapper.INSTANCE.toDto(categoryDao.findById(id));
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryDao.findAll(pageable).stream().map(CategoryMapper.INSTANCE::toDto).toList();
    }
}
