package ru.practicum.service.dto.event.comment.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.service.dto.event.comment.ShortCommentDto;
import ru.practicum.service.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
public class ReportInfoDto {
    private Long id;
    private ShortCommentDto comment;
    private UserShortDto user;
    private String reportReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportDate;
    private String description;
    private String state;
}
