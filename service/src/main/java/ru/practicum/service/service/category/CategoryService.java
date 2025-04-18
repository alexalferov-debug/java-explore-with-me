package ru.practicum.service.service.category;

import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto patchCategory(Long id, NewCategoryDto categoryDto);

    CategoryDto getCategory(Long id);

    List<CategoryDto> getCategories(int from, int size);
}
