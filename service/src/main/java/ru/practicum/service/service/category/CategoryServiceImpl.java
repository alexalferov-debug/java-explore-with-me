package ru.practicum.service.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dao.category.CategoryDao;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;
import ru.practicum.service.mapper.CategoryMapper;
import ru.practicum.service.model.Category;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        return CategoryMapper.INSTANCE.toDto(categoryDao.save(CategoryMapper.INSTANCE.fromDto(categoryDto)));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
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
