package ru.practicum.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
