package ru.practicum.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.service.data.EventStateAction;
import ru.practicum.service.dto.location.Location;
import ru.practicum.service.validation.EventDateConstraint;

import java.time.LocalDateTime;

@Data
@Validated
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2_000)
    private String annotation;
    @PositiveOrZero
    private Long category;
    @Size(min = 20, max = 7_000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EventDateConstraint(amountInHours = 1)
    @Future
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
