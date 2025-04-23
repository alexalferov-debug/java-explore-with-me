package ru.practicum.service.dto.event.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Добавление нового комментария")
public class AddCommentDto {
    @Schema(description = "Комментарий, добавляемый пользователем", example = "Отличное событие! Весело и просто!", minimum = "20", maximum = "5000")
    @NotBlank
    @Size(min = 10, max = 5000)
    private String content;
}
