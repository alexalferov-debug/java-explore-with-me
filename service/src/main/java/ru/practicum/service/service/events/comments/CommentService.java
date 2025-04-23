package ru.practicum.service.service.events.comments;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.dto.event.comment.AddCommentDto;
import ru.practicum.service.dto.event.comment.FullCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDtoWithEvent;
import ru.practicum.service.dto.event.comment.report.NewReportDto;
import ru.practicum.service.dto.event.comment.report.ReportAddNote;
import ru.practicum.service.dto.event.comment.report.ReportFullInfoDto;
import ru.practicum.service.dto.event.comment.report.ReportInfoDto;
import ru.practicum.service.model.ReportReasons;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    ShortCommentDto addComment(Long userId, Long eventId, HttpServletRequest requestData, AddCommentDto addCommentDto);

    ShortCommentDto addReply(Long userId, Long commentId, HttpServletRequest requestData, AddCommentDto addCommentDto);

    ShortCommentDto patchComment(Long userId, Long commentId, AddCommentDto addCommentDto);

    void softDeleteComment(Long userId, Long commentId);

    void deleteComment(Long commentId);

    ShortCommentDto getComment(Long commentId);

    List<ShortCommentDto> getComments(Long eventId, int from, int size);

    List<ShortCommentDto> getReplies(Long commentId, int from, int size);

    List<ShortCommentDtoWithEvent> getCommentsForUser(Long userId, int from, int size);

    List<FullCommentDto> getCommentsFromAdmin(List<Long> userIds,
                                              List<Long> eventIds,
                                              LocalDateTime createdFrom,
                                              LocalDateTime createdTo,
                                              Boolean isDeleted,
                                              Boolean hasReplies,
                                              String content,
                                              int from,
                                              int size);

    List<ReportFullInfoDto> getReportsForAdmin(List<Long> eventsId,
                                               List<Long> reporterId,
                                               List<Long> commenterId,
                                               List<Long> commentsId,
                                               List<Long> reason,
                                               LocalDateTime createdFrom,
                                               LocalDateTime createdTo,
                                               List<CommentReportState> state,
                                               int from,
                                               int size);

    ReportInfoDto addReport(Long userId, Long commentId, NewReportDto newReportDto);

    ReportInfoDto getReportInfo(Long userId, Long reportId);

    List<ReportInfoDto> getReportInfos(Long userId, CommentReportState state, int from, int size);

    List<ReportReasons> getReportReasons();

    ReportFullInfoDto addReportNote(Long reportId, ReportAddNote note);

    ReportFullInfoDto resolveReport(Long reportId, CommentReportState state);
}
