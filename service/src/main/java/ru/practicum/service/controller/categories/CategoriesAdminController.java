package ru.practicum.service.controller.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.category.NewCategoryDto;
import ru.practicum.service.service.category.CategoryService;

@RestController
@RequestMapping("/admin/categories")
public class CategoriesAdminController {
    CategoryService categoryService;

    @Autowired
    public CategoriesAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Validated NewCategoryDto categoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<CategoryDto> delete(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long catId, @RequestBody @Validated NewCategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.patchCategory(catId, categoryDto));
    }
}
