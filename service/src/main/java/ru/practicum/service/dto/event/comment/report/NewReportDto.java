package ru.practicum.service.dto.event.comment.report;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewReportDto {
    @NotNull
    private Long reasonId;
    @Size(min = 50, max = 500)
    private String description;
}
