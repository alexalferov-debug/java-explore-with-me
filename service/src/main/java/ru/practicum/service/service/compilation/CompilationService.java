package ru.practicum.service.service.compilation;

import ru.practicum.service.dto.compilation.CompilationDto;
import ru.practicum.service.dto.compilation.NewCompilationDto;
import ru.practicum.service.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto patchCompilation(long id, UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilation(long id);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(long id);
}
