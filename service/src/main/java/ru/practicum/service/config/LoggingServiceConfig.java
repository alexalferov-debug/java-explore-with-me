package ru.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.service.dao.category.CategoryDao;
import ru.practicum.service.dao.compilation.CompilationDao;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.logging.*;
import ru.practicum.service.repository.LocationRepository;
import ru.practicum.service.repository.RequestRepository;
import ru.practicum.service.service.category.CategoryService;
import ru.practicum.service.service.category.CategoryServiceImpl;
import ru.practicum.service.service.compilation.CompilationService;
import ru.practicum.service.service.compilation.CompilationServiceImpl;
import ru.practicum.service.service.events.EventViewService;
import ru.practicum.service.service.events.EventsService;
import ru.practicum.service.service.events.EventsServiceImpl;
import ru.practicum.service.service.request.RequestsService;
import ru.practicum.service.service.request.RequestsServiceImpl;
import ru.practicum.service.service.user.UserService;
import ru.practicum.service.service.user.UserServiceImpl;

@Configuration
public class LoggingServiceConfig {

    @Bean
    public CategoryService categoryService(CategoryDao categoryDao, EventDao eventDao) {
        CategoryService service = new CategoryServiceImpl(categoryDao, eventDao);
        service = new LoggingCategoryDecorator(service);
        return service;
    }

    @Bean
    public CompilationService compilationService(CompilationDao compilationDao, EventDao eventDao) {
        CompilationService service = new CompilationServiceImpl(compilationDao, eventDao);
        service = new LoggingCompilationDecorator(service);
        return service;
    }

    @Bean
    public EventsService eventsService(EventDao repo,
                                       UserDao userDao,
                                       CategoryDao categoryDao,
                                       LocationRepository locationRepository,
                                       RequestRepository requestRepository,
                                       EventViewService eventViewService) {
        EventsService service = new EventsServiceImpl(repo,
                locationRepository,
                userDao,
                categoryDao,
                requestRepository,
                eventViewService);
        service = new LoggingEventsDecorator(service);
        return service;
    }

    @Bean
    public RequestsService requestsService(RequestRepository requestRepository,
                                           UserDao userDao,
                                           EventDao eventDao) {
        RequestsService service = new RequestsServiceImpl(requestRepository, userDao, eventDao);
        service = new LoggingRequestDecorator(service);
        return service;
    }

    @Bean
    public UserService userService(UserDao repo) {
        UserService service = new UserServiceImpl(repo);
        service = new LoggingUserDecorator(service);
        return service;
    }
}
