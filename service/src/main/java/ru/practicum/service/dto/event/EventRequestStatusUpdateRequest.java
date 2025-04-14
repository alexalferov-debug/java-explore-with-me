package ru.practicum.service.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.service.data.RequestStatus;

import java.util.List;

@Data
@Validated
public class EventRequestStatusUpdateRequest {
    @NotNull
    List<Long> requestIds;
    @NotNull
    RequestStatus status;
}
