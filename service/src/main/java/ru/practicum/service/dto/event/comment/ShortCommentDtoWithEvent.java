package ru.practicum.service.dto.event.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Schema(description = "Подробная информация о комментарии для пользователя")
public class ShortCommentDtoWithEvent {
    @Schema(description = "Идентификатор комментария", example = "1")
    private Long id;

    @Schema(description = "идентификатор события")
    private EventShortDto event;

    @Schema(description = "Информация о пользователе, оставившем комментарий")
    private UserShortDto author;

    @Schema(description = "Текст комментария")
    private String content;

    @Schema(description = "Дата добавления комментария")
    private LocalDateTime createdAt;

    @Schema(description = "Дата изменения комментария")
    private LocalDateTime updatedAt;

    @Schema(description = "Были ли добавлены ответы для комментария")
    private Boolean hasReplies;

    private ShortCommentDto parent;
}
