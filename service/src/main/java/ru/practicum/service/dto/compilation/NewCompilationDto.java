package ru.practicum.service.dto.compilation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
public class NewCompilationDto {
    private Boolean pinned = false;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    @JsonProperty(value = "events")
    @UniqueElements
    private List<Long> eventsId;
}
