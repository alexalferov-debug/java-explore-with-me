package ru.practicum.service.controller.events.comments;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.dto.event.comment.AddCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDtoWithEvent;
import ru.practicum.service.dto.event.comment.report.NewReportDto;
import ru.practicum.service.dto.event.comment.report.ReportInfoDto;
import ru.practicum.service.model.ApiError;
import ru.practicum.service.service.events.comments.CommentService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentsController {

    CommentService commentService;

    public PrivateCommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Добавление комментария",
            description = "Позволяет пользователю оставить комментарий к опубликованному событию",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Комментарий успешно добавлен",
                            content = @Content(schema = @Schema(implementation = ShortCommentDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Событие не найдено или не опубликовано",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            name = "NotFound",
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Event with id=999 not found\",\"timestamp\":\"2024-01-01T12:00:00\",\"errors\":[]}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Невалидные данные запроса",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            name = "Validation Error",
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"Incorrectly made request.\",\"message\":\"Ошибка валидации\",\"timestamp\":\"2024-01-01T12:00:00\",\"errors\":[\"content: must not be blank\"]}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/{eventId}")
    public ResponseEntity<ShortCommentDto> addComment(@Parameter(description = "ID пользователя", example = "123", required = true)
                                                      @PathVariable @PositiveOrZero Long userId,
                                                      @Parameter(description = "ID события", example = "456", required = true)
                                                      @PathVariable @PositiveOrZero Long eventId,
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                              description = "Данные комментария",
                                                              required = true,
                                                              content = @Content(schema = @Schema(implementation = AddCommentDto.class))
                                                      )
                                                      @RequestBody @Validated AddCommentDto commentDto,
                                                      HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(userId, eventId, request, commentDto));
    }

    @Operation(
            summary = "Редактирование комментария",
            description = "Позволяет автору комментария изменить его содержимое",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ShortCommentDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Comment with id=789 not found\",\"timestamp\":\"2024-01-01T12:00:00\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"For the requested operation the conditions are not met.\",\"message\":\"Нельзя редактировать чужие комментарии\",\"timestamp\":\"2024-01-01T12:00:00\"}"
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/{commentId}")
    public ResponseEntity<ShortCommentDto> updateComment(@Parameter(description = "ID пользователя", example = "123", required = true)
                                                         @PathVariable @PositiveOrZero Long userId,

                                                         @Parameter(description = "ID комментария", example = "789", required = true)
                                                         @PathVariable @PositiveOrZero Long commentId,

                                                         @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                 description = "Обновленные данные комментария",
                                                                 required = true,
                                                                 content = @Content(schema = @Schema(implementation = AddCommentDto.class)))
                                                         @RequestBody @Validated AddCommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.patchComment(userId, commentId, commentDto));
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Помечает комментарий как удаленный (мягкое удаление). Если комментарий помечен как удаленный - все комментарии с ответами на текущий тоже помечаются как удаленные. Если комментарий уже помечен как удаленнный - вернуть 404.",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Comment with id=789 not found\",\"timestamp\":\"2024-01-01T12:00:00\"}"
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@Parameter(description = "ID пользователя", example = "123", required = true)
                                              @PathVariable @PositiveOrZero Long userId,

                                              @Parameter(description = "ID комментария", example = "789", required = true)
                                              @PathVariable @PositiveOrZero Long commentId) {
        commentService.softDeleteComment(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Добавление ответа на комментарий",
            description = "Добавляет ответ на комментарий. Ответ можно добавить только к неудаленному комментарию.",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Comment with id=789 not found\",\"timestamp\":\"2024-01-01T12:00:00\"}"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/reply/{commentId}")
    public ResponseEntity<ShortCommentDto> addReply(@Parameter(description = "Идентификатор комментария", example = "10")
                                                    @PathVariable Long commentId,
                                                    @Parameter(description = "Идентификатор пользователя", example = "10")
                                                    @PathVariable Long userId,
                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                            description = "Текст комментария",
                                                            required = true,
                                                            content = @Content(schema = @Schema(implementation = AddCommentDto.class))
                                                    )
                                                    @RequestBody @Validated AddCommentDto commentDto,
                                                    HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addReply(userId, commentId, request, commentDto));
    }

    @GetMapping
    @Operation(
            summary = "Получение комментариев пользователя",
            description = "Возвращает список всех комментариев пользователя с указанием события",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = ShortCommentDtoWithEvent[].class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"User with id=999 not found\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<List<ShortCommentDtoWithEvent>> getComments(@Parameter(description = "ID пользователя", example = "123", required = true)
                                                                      @PathVariable @PositiveOrZero Long userId,
                                                                      @Parameter(description = "Начальная позиция", example = "0")
                                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                      @Parameter(description = "Размер страницы", example = "10")
                                                                      @RequestParam(defaultValue = "10") @PositiveOrZero int size) {
        return ResponseEntity.ok(commentService.getCommentsForUser(userId, from, size));
    }

    @PostMapping("/{commentId}/report")
    @Operation(
            summary = "Создание жалобы на комментарий",
            description = "Позволяет пользователю пожаловаться на комментарий",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReportInfoDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Комментарий не найден",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Comment with id=789 not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Невалидные данные жалобы",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"Incorrectly made request.\",\"message\":\"Ошибка валидации\",\"errors\":[\"reason: must not be blank\"]}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ReportInfoDto> addReport(@Parameter(description = "ID комментария", example = "789", required = true)
                                                   @PathVariable @PositiveOrZero Long commentId,
                                                   @Parameter(description = "ID пользователя", example = "123", required = true)
                                                   @PathVariable @PositiveOrZero Long userId,
                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                           description = "Данные жалобы",
                                                           required = true,
                                                           content = @Content(schema = @Schema(implementation = NewReportDto.class))
                                                   )
                                                   @RequestBody @Validated NewReportDto reportDto) {
        return ResponseEntity.ok(commentService.addReport(userId, commentId, reportDto));
    }

    @GetMapping("/report/{reportId}")
    @Operation(
            summary = "Получение информации о жалобе",
            description = "Возвращает детали конкретной жалобы пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReportInfoDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Жалоба не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"NOT_FOUND\",\"reason\":\"The required object was not found.\",\"message\":\"Report with id=456 not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"FORBIDDEN\",\"reason\":\"Access denied\",\"message\":\"You can't view this report\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ReportInfoDto> getReport(@Parameter(description = "ID жалобы", example = "456", required = true)
                                                   @PathVariable @PositiveOrZero Long reportId,
                                                   @Parameter(description = "ID пользователя", example = "123", required = true)
                                                   @PathVariable @PositiveOrZero Long userId) {
        return ResponseEntity.ok(commentService.getReportInfo(userId, reportId));
    }

    @GetMapping("/report/")
    @Operation(
            summary = "Получение списка жалоб",
            description = "Возвращает список жалоб пользователя с возможностью фильтрации по статусу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ReportInfoDto[].class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Неверные параметры запроса",
                            content = @Content(
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(
                                            value = "{\"status\":\"BAD_REQUEST\",\"reason\":\"Incorrectly made request.\",\"message\":\"Invalid parameter: state\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<List<ReportInfoDto>> getReports(@Parameter(description = "ID пользователя", example = "123", required = true)
                                                          @PathVariable @PositiveOrZero Long userId,
                                                          @Parameter(description = "Фильтр по статусу жалобы")
                                                          @RequestParam(required = false) CommentReportState state,
                                                          @Parameter(description = "Начальная позиция", example = "0")
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @Parameter(description = "Размер страницы", example = "10")
                                                          @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getReportInfos(userId, state, from, size));
    }
}
