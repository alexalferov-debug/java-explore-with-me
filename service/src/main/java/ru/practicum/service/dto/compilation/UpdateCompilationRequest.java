package ru.practicum.service.dto.compilation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private Boolean pinned;
    @Size(min = 1, max = 50)
    private String title;
    @JsonProperty(value = "events")
    private List<Long> eventsId;
}
