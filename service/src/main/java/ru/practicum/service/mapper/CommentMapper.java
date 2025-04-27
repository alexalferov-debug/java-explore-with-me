package ru.practicum.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.dto.event.comment.FullCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDtoWithEvent;
import ru.practicum.service.dto.event.comment.report.ReportFullInfoDto;
import ru.practicum.service.dto.event.comment.report.ReportInfoDto;
import ru.practicum.service.model.Comment;
import ru.practicum.service.model.CommentReport;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    ShortCommentDtoWithEvent toShortCommentDtoWithEvent(Comment comment);

    @Mapping(source = "comment.event.id", target = "eventId")
    ShortCommentDto toShortCommentDto(Comment comment);

    FullCommentDto toFullCommentDto(Comment comment);

    @Mapping(source = "reason.description", target = "reportReason")
    @Mapping(source = "status", target = "state")
    @Mapping(source = "createdAt", target = "reportDate")
    @Mapping(source = "reporter", target = "user")
    ReportFullInfoDto toReportFullInfoDto(CommentReport comment);

    @Mapping(source = "reason.description", target = "reportReason")
    @Mapping(source = "status", target = "state")
    @Mapping(source = "createdAt", target = "reportDate")
    @Mapping(source = "reporter", target = "user")
    ReportInfoDto toReportInfoDto(CommentReport comment);

    default String mapCommentReportState(CommentReportState state) {
        return state != null ? state.getName() : null;
    }

}
