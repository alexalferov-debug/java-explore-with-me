package ru.practicum.service.controller.events.comments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.dto.event.comment.FullCommentDto;
import ru.practicum.service.dto.event.comment.report.ReportAddNote;
import ru.practicum.service.dto.event.comment.report.ReportFullInfoDto;
import ru.practicum.service.model.ApiError;
import ru.practicum.service.service.events.comments.CommentService;
import ru.practicum.service.service.events.comments.CommentServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
public class AdminCommentsController {

    CommentService commentService;

    public AdminCommentsController(CommentServiceImpl commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Поиск комментариев",
            description = "Административный поиск комментариев с расширенными фильтрами",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = FullCommentDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные параметры запроса",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"Incorrect date range\",\"message\":\"createdFrom must be before createdTo\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<FullCommentDto>> getAllComments(@Parameter(description = "Список ID пользователей", example = "[123, 456]")
                                                               @RequestParam(required = false) List<Long> userIds,
                                                               @Parameter(description = "Список ID событий", example = "[789, 101]")
                                                               @RequestParam(required = false) List<Long> eventIds,
                                                               @Parameter(description = "Дата создания ОТ", example = "2024-01-01 00:00:00")
                                                               @RequestParam(required = false)
                                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdFrom,
                                                               @Parameter(description = "Дата создания ДО", example = "2024-12-31 23:59:59")
                                                               @RequestParam(required = false)
                                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdTo,
                                                               @Parameter(description = "Фильтр по удаленным комментариям")
                                                               @RequestParam(required = false) Boolean isDeleted,
                                                               @Parameter(description = "Фильтр по наличию ответов")
                                                               @RequestParam(required = false) Boolean hasReplies,
                                                               @Parameter(description = "Поиск по содержимому", example = "спам")
                                                               @RequestParam(required = false) String content,
                                                               @Parameter(description = "Начальная позиция", example = "0")
                                                               @RequestParam(defaultValue = "0") int from,
                                                               @Parameter(description = "Размер страницы", example = "10")
                                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsFromAdmin(userIds,
                eventIds,
                createdFrom,
                createdTo,
                isDeleted,
                hasReplies,
                content,
                from,
                size));
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Полное удаление комментария из системы (только для администраторов)",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"Comment not found\",\"message\":\"Comment with id=999 doesn't exist\"}"
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@Parameter(description = "ID комментария", example = "789", required = true)
                                              @PathVariable @PositiveOrZero Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Добавление заметки к жалобе",
            description = "Позволяет администратору добавить внутреннюю заметку к жалобе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReportFullInfoDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"Report not found\",\"message\":\"Report with id=456 doesn't exist\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"Validation error\",\"message\":\"Note text is required\"}"
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/report/{reportId}/note")
    public ResponseEntity<ReportFullInfoDto> addNote(@PathVariable @PositiveOrZero Long reportId,
                                                     @RequestBody @Validated ReportAddNote addNote) {
        return ResponseEntity.ok(commentService.addReportNote(reportId, addNote));
    }

    @Operation(
            summary = "Изменение статуса жалобы",
            description = "Позволяет изменить статус обработки жалобы. Из статуса \"Ожидает\" возможен переход только в \"Принято\" и \"Отклонено\", из статусов \"Принято\" и \"Отклонено\" только в \"Ожидает\". Если жалоба принята - комментарий помечается на удаление. Если жалоба обработана - остальные жалобы на тот же комментарий переходят в соответствуйщий статус.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReportFullInfoDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"Report not found\",\"message\":\"Report with id=456 doesn't exist\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"Invalid state\",\"message\":\"Cannot transition from CLOSED to IN_PROGRESS\"}"
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/report/{reportId}/resolve")
    public ResponseEntity<ReportFullInfoDto> resolveReport(@Parameter(description = "ID жалобы", example = "456", required = true)
                                                           @PathVariable @PositiveOrZero Long reportId,
                                                           @Parameter(description = "Новый статус", example = "RESOLVED", required = true)
                                                           @RequestParam CommentReportState newState) {
        return ResponseEntity.ok(commentService.resolveReport(reportId, newState));
    }

    @Operation(
            summary = "Поиск жалоб",
            description = "Административный поиск жалоб с расширенными фильтрами",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ReportFullInfoDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"Invalid parameter\",\"message\":\"Unknown report state: INVALID_STATE\"}"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/reports")
    public ResponseEntity<List<ReportFullInfoDto>> getAllReports(@Parameter(description = "Список ID событий", example = "[789, 101]")
                                                                 @RequestParam(required = false) List<Long> eventsId,
                                                                 @Parameter(description = "Список ID событий", example = "[789, 101]")
                                                                 @RequestParam(required = false) List<Long> reporterId,
                                                                 @Parameter(description = "Список ID авторов комментариев", example = "[789, 101]")
                                                                 @RequestParam(required = false) List<Long> commenterId,
                                                                 @Parameter(description = "Список ID комментариев", example = "[456, 789]")
                                                                 @RequestParam(required = false) List<Long> commentsId,
                                                                 @Parameter(description = "Список ID причин жалоб", example = "[1, 3]")
                                                                 @RequestParam(required = false) List<Long> reason,
                                                                 @Parameter(description = "Дата создания ОТ", example = "2024-01-01 00:00:00")
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                 @RequestParam(required = false) LocalDateTime createdFrom,
                                                                 @Parameter(description = "Дата создания ДО", example = "2024-12-31 23:59:59")
                                                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                 @RequestParam(required = false) LocalDateTime createdTo,
                                                                 @Parameter(description = "Список статусов жалоб")
                                                                 @RequestParam(required = false) List<CommentReportState> state,
                                                                 @Parameter(description = "Начальная позиция", example = "0")
                                                                 @RequestParam(defaultValue = "0") int from,
                                                                 @Parameter(description = "Размер страницы", example = "10")
                                                                 @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getReportsForAdmin(eventsId,
                reporterId,
                commenterId,
                commentsId,
                reason,
                createdFrom,
                createdTo,
                state,
                from,
                size));
    }
}
