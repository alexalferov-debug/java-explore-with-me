package ru.practicum.service.dto.event;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Validated
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
