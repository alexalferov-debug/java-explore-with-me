package ru.practicum.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.service.dto.location.Location;
import ru.practicum.service.validation.EventDateConstraint;

import java.time.LocalDateTime;

@Data
@Validated
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2_000)
    private String annotation;
    @NotNull
    @PositiveOrZero
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7_000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    @EventDateConstraint
    @Future
    private LocalDateTime eventDate;
    private Location location;
    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean paid = false;
    private Boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
