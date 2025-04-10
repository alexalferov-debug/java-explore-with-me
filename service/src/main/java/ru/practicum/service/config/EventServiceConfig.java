package ru.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.service.dao.category.CategoryDao;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.logging.LoggingEventsDecorator;
import ru.practicum.service.repository.LocationRepository;
import ru.practicum.service.service.events.EventsService;
import ru.practicum.service.service.events.EventsServiceImpl;

@Configuration
public class EventServiceConfig {
    @Bean
    public EventsService eventsService(EventDao repo,
                                     UserDao userDao,
                                     CategoryDao categoryDao,
                                     LocationRepository locationRepository) {
        EventsService service = new EventsServiceImpl(repo, locationRepository, userDao, categoryDao);
        service = new LoggingEventsDecorator(service);
        return service;
    }
}
