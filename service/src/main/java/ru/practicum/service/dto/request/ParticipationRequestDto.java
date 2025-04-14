package ru.practicum.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.service.data.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSS")
    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    RequestStatus status;
}
