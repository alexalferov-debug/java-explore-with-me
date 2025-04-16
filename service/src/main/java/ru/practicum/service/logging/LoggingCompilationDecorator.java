package ru.practicum.service.logging;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.service.dto.compilation.CompilationDto;
import ru.practicum.service.dto.compilation.NewCompilationDto;
import ru.practicum.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.service.compilation.CompilationService;

import java.util.List;
import java.util.Objects;

@Slf4j
public class LoggingCompilationDecorator implements CompilationService {

    CompilationService delegate;

    public LoggingCompilationDecorator(CompilationService delegate) {
        this.delegate = delegate;
    }


    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.debug("Запрос на добавление подборки: {}", newCompilationDto);
        return delegate.addCompilation(newCompilationDto);
    }

    @Override
    public CompilationDto patchCompilation(long id, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Запрос на изменение подборки {}: {}", id, updateCompilationRequest);
        return delegate.patchCompilation(id, updateCompilationRequest);
    }

    @Override
    public void deleteCompilation(long id) {
        log.info("Запрос на удаление подборки {}", id);
        delegate.deleteCompilation(id);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        String pinnedString = "всех";
        if (Objects.nonNull(pinned) && !pinned) {
            pinnedString = "не закрепленных";
        }
        if (Objects.nonNull(pinned) && pinned) {
            pinnedString = "закрепленных";
        }
        log.info("Запрошен список {} подборок: from = {}, size = {}", pinnedString, from, size);
        return delegate.getCompilations(pinned, from, size);
    }

    @Override
    public CompilationDto getCompilation(long id) {
        return delegate.getCompilation(id);
    }
}
