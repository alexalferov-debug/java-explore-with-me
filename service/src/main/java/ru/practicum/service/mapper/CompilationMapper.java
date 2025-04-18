package ru.practicum.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.service.dto.compilation.CompilationDto;
import ru.practicum.service.dto.compilation.NewCompilationDto;
import ru.practicum.service.model.Compilation;

@Mapper
public interface CompilationMapper {
    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

    CompilationDto toDto(Compilation compilation);

    Compilation fromDto(NewCompilationDto compilationDto);
}
