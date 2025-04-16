package ru.practicum.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.service.dao.compilation.CompilationDao;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.logging.LoggingCompilationDecorator;
import ru.practicum.service.service.compilation.CompilationService;
import ru.practicum.service.service.compilation.CompilationServiceImpl;

@Configuration
public class CompilationServiceConfig {
    @Bean
    public CompilationService compilationService(CompilationDao compilationDao, EventDao eventDao) {
        CompilationService service = new CompilationServiceImpl(compilationDao, eventDao);
        service = new LoggingCompilationDecorator(service);
        return service;
    }
}
