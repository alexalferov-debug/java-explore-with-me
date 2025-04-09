package ru.practicum.service.controller.categories;

import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesPublicController {
    CategoryService categoryService;

    @Autowired
    public CategoriesPublicController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0")
                                              @Min(0)
                                              int from,
                                              @RequestParam(defaultValue = "10")
                                              @Min(1)
                                              int size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }
}
