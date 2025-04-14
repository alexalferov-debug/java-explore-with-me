package ru.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.logging.LoggingRequestDecorator;
import ru.practicum.service.repository.RequestRepository;
import ru.practicum.service.service.request.RequestsService;
import ru.practicum.service.service.request.RequestsServiceImpl;

@Configuration
public class RequestServiceConfig {

    @Bean
    public RequestsService requestsService(RequestRepository requestRepository,
                                           UserDao userDao,
                                           EventDao eventDao) {
        RequestsService service = new RequestsServiceImpl(requestRepository, userDao, eventDao);
        service = new LoggingRequestDecorator(service);
        return service;
    }
}
