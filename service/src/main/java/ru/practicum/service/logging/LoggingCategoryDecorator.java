package ru.practicum.service.logging;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;
import ru.practicum.service.service.category.CategoryService;

import java.util.List;

@Slf4j
public class LoggingCategoryDecorator implements CategoryService {
    private final CategoryService categoryService;

    public LoggingCategoryDecorator(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        log.info("Create new category: {}", categoryDto);
        return categoryService.createCategory(categoryDto);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Delete category: {}", id);
        categoryService.deleteCategory(id);
    }

    @Override
    public CategoryDto patchCategory(Long id, NewCategoryDto categoryDto) {
        log.info("Patch category with id = {}, new required name = {}", id, categoryDto);
        return categoryService.patchCategory(id, categoryDto);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        log.info("Get category with id = {}", id);
        return categoryService.getCategory(id);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        log.info("Get {} categories from {}", size, from);
        return categoryService.getCategories(from, size);
    }
}
