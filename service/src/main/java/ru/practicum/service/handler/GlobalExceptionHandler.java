package ru.practicum.service.handler;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.service.exception.EventValidationException;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.model.ApiError;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleConstraintViolation(DataIntegrityViolationException ex) {
        String rootMsg = getRootCause(ex).getMessage();
        String constraint = extractConstraintName(rootMsg);

        String message = switch (constraint) {
            case "uq_email" -> "Email уже занят";
            case "uq_category_name" -> "Категория с таким именем уже существует";
            case "uq_location" -> "Локация уже существует";
            default -> "Нарушение уникальности данных";
        };

        List<String> errors = List.of(rootMsg);
        return buildResponse(HttpStatus.CONFLICT, "Integrity constraint violated", message, errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "The required object was not found.",
                ex.getMessage(), List.of());
    }

    @ExceptionHandler(EventValidationException.class)
    public ResponseEntity<ApiError> handleEventValidation(EventValidationException ex) {
        return buildResponse(HttpStatus.CONFLICT, "For the requested operation the conditions are not met.",
                ex.getMessage(), List.of());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleEventValidation(ValidationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "For the requested operation the conditions are not met.",
                ex.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();

        boolean isEventDateError = fieldErrors.stream()
                .anyMatch(fe ->
                        "eventDate".equals(fe.getField()) &&
                                "EventDateConstraint".equals(fe.getCode())
                );

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String reason = "Incorrectly made request.";
        String message = "Ошибка валидации";

        if (isEventDateError) {
            status = HttpStatus.CONFLICT;
            reason = "For the requested operation the conditions are not met.";
            message = fieldErrors.stream()
                    .filter(fe -> "eventDate".equals(fe.getField()))
                    .findFirst()
                    .map(FieldError::getDefaultMessage)
                    .orElse(message);
        }

        List<String> errors = fieldErrors.stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());

        return buildResponse(status, reason, message, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        Class<?> requiredType = ex.getRequiredType();

        String typeName = requiredType != null
                ? requiredType.getSimpleName()
                : "неизвестный тип";

        String errorMessage = String.format(
                "Параметр '%s' должен быть типа %s",
                parameterName,
                typeName
        );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                errorMessage,
                List.of(errorMessage)
        );
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String reason,
                                                   String message, List<String> errors) {
        return ResponseEntity
                .status(status)
                .body(new ApiError(
                        status.name(),
                        reason,
                        message,
                        LocalDateTime.now(),
                        errors.isEmpty() ? Collections.emptyList() : errors
                ));
    }

    private Throwable getRootCause(Throwable ex) {
        return ex.getCause() == null ? ex : getRootCause(ex.getCause());
    }

    private String extractConstraintName(String message) {
        Matcher matcher = Pattern.compile("constraint \\[\"(.*?)\"\\]").matcher(message);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

}