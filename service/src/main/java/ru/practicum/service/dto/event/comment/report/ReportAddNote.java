package ru.practicum.service.dto.event.comment.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportAddNote {
    @NotBlank
    @Size(min = 5, max = 50000)
    private String notes;
}
