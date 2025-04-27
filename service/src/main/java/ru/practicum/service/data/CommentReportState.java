package ru.practicum.service.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentReportState {
    PENDING("В ожидании"),
    REJECTED("Отклонено"),
    RESOLVED("Принято");

    private final String name;
}
