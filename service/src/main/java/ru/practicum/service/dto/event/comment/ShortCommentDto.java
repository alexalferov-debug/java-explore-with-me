package ru.practicum.service.dto.event.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.practicum.service.dto.user.UserShortDto;

@Data
@Schema(description = "Подробная информация о комментарии")
public class ShortCommentDto {
    @Schema(description = "Идентификатор комментария", example = "1")
    private Long id;

    @Schema(description = "Идентификатор события")
    private Long eventId;

    @Schema(description = "Информация о пользователе, оставившем комментарий")
    private UserShortDto author;

    @Schema(description = "Текст комментария")
    private String content;

    @Schema(description = "Были ли добавлены ответы для комментария")
    private Boolean hasReplies;
}
