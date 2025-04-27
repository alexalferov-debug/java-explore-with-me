package ru.practicum.service.controller.events.comments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.event.comment.ShortCommentDto;
import ru.practicum.service.model.ApiError;
import ru.practicum.service.model.ReportReasons;
import ru.practicum.service.service.events.comments.CommentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class PublicCommentsController {
    CommentServiceImpl commentService;

    public PublicCommentsController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Получение комментария",
            description = "Возвращает комментарий по его ID (только неудаленные)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ShortCommentDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Комментарий не найден или удален",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Comment with id=789 not found\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{commentId}")
    public ResponseEntity<ShortCommentDto> getComment(@Parameter(description = "ID комментария", example = "789", required = true)
                                                      @PathVariable @PositiveOrZero Long commentId) {
        return ResponseEntity.ok(commentService.getComment(commentId));
    }

    @Operation(
            summary = "Получение ответов на комментарий",
            description = "Возвращает список ответов на комментарий с пагинацией",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ShortCommentDto[].class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Родительский комментарий не найден или удален",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Parent comment with id=789 not found\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{commentId}/reply")
    public ResponseEntity<List<ShortCommentDto>> getReplies(@Parameter(description = "ID родительского комментария", example = "789", required = true)
                                                            @PathVariable @PositiveOrZero Long commentId,
                                                            @Parameter(description = "Начальная позиция", example = "0")
                                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                            @Parameter(description = "Размер страницы", example = "10")
                                                            @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        return ResponseEntity.ok(commentService.getReplies(commentId, from, size));
    }

    @Operation(
            summary = "Получение списка причин для жалоб",
            description = "Возвращает доступные причины для создания жалобы на комментарии",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReportReasons[].class))
                    )
            }
    )
    @GetMapping("/reportReason")
    public ResponseEntity<List<ReportReasons>> getReportReasons() {
        return ResponseEntity.ok(commentService.getReportReasons());
    }
}
