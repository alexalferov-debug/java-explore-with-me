package ru.practicum.service.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
public class EventRequestStatusUpdateRequest {
    @NotNull
    List<Integer> requestIds;
    @NotBlank
    String status;
}
