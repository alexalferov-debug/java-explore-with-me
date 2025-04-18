package ru.practicum.service.service.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dao.compilation.CompilationDao;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dto.compilation.CompilationDto;
import ru.practicum.service.dto.compilation.NewCompilationDto;
import ru.practicum.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.mapper.CompilationMapper;
import ru.practicum.service.model.Compilation;
import ru.practicum.service.model.Event;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    CompilationDao compilationDao;
    EventDao eventDao;

    @Autowired
    public CompilationServiceImpl(CompilationDao compilationDao,
                                  EventDao eventDao) {
        this.compilationDao = compilationDao;
        this.eventDao = eventDao;
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (Objects.nonNull(newCompilationDto.getEventsId()) && !newCompilationDto.getEventsId().isEmpty()) {
            events = eventDao.findByIdIn(newCompilationDto.getEventsId());
        }
        Compilation compilation = CompilationMapper.INSTANCE.fromDto(newCompilationDto);
        compilation.setEvents(new LinkedHashSet<>(events));
        return CompilationMapper.INSTANCE.toDto(compilationDao.save(compilation));
    }

    @Override
    @Transactional
    public CompilationDto patchCompilation(long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationDao.get(id);
        if (Objects.nonNull(updateCompilationRequest.getTitle())) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (Objects.nonNull(updateCompilationRequest.getPinned()) && !compilation.getPinned().equals(updateCompilationRequest.getPinned())) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (Objects.nonNull(updateCompilationRequest.getEventsId())) {
            compilation.setEvents(new LinkedHashSet<>(eventDao.findByIdIn(updateCompilationRequest.getEventsId())));
        }
        return CompilationMapper.INSTANCE.toDto(compilationDao.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(long id) {
        compilationDao.get(id);
        compilationDao.delete(id);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationDao.getAllWithPinned(pinned, pageable).stream().map(CompilationMapper.INSTANCE::toDto).toList();
    }

    @Override
    public CompilationDto getCompilation(long id) {
        return CompilationMapper.INSTANCE.toDto(compilationDao.get(id));
    }
}
