package ru.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.service.dao.category.CategoryDao;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.logging.LoggingCategoryDecorator;
import ru.practicum.service.service.category.CategoryService;
import ru.practicum.service.service.category.CategoryServiceImpl;

@Configuration
public class CategoryServiceConfig {
    @Bean
    public CategoryService categoryService(CategoryDao categoryDao, EventDao eventDao) {
        CategoryService service = new CategoryServiceImpl(categoryDao, eventDao);
        service = new LoggingCategoryDecorator(service);
        return service;
    }
}
