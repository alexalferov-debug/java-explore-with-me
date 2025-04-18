package ru.practicum.service.dto.compilation;

import lombok.Data;
import ru.practicum.service.dto.event.EventFullDto;

import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private List<EventFullDto> events;
}
