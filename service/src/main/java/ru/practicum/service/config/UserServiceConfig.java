package ru.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.logging.LoggingUserDecorator;
import ru.practicum.service.service.user.UserService;
import ru.practicum.service.service.user.UserServiceImpl;

@Configuration
public class UserServiceConfig {
    @Bean
    public UserService userService(UserDao repo) {
        UserService service = new UserServiceImpl(repo);
        service = new LoggingUserDecorator(service);
        return service;
    }
}
