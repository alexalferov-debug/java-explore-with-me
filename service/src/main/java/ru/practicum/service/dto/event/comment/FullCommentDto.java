package ru.practicum.service.dto.event.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
public class FullCommentDto {
    private Long id;
    private EventShortDto event;
    private UserShortDto author;
    private String content;
    private String commenterIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Boolean hasReplies;
    private Boolean isDeleted;
    private ShortCommentDto parent;
}
