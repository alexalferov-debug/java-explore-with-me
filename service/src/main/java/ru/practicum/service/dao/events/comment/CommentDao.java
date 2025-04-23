package ru.practicum.service.dao.events.comment;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.model.Comment;
import ru.practicum.service.model.CommentReport;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentDao {
    Comment create(Comment comment);

    void delete(Long id);

    Comment update(Comment comment);

    Comment findById(Long id);

    Comment findNotDeletedById(Long id);

    List<Comment> findAllForEvent(Long eventId, Pageable pageable);

    List<Comment> findAllForUser(Long userId, Pageable pageable);

    List<Comment> findAllReplies(Long eventId, Pageable pageable);

    List<Comment> findCommentsForAdmin(List<Long> userIds,
                                       List<Long> eventIds,
                                       LocalDateTime createdFrom,
                                       LocalDateTime createdTo,
                                       Boolean isDeleted,
                                       Boolean hasReplies,
                                       String content,
                                       Pageable pageable);

    List<CommentReport> getReportsForAdmin(List<Long> eventsId,
                                           List<Long> reporterId,
                                           List<Long> commenterId,
                                           List<Long> commentsId,
                                           List<Long> reason,
                                           LocalDateTime createdFrom,
                                           LocalDateTime createdTo,
                                           List<CommentReportState> state,
                                           Pageable pageable);

    CommentReport saveReport(CommentReport report);

    CommentReport updateReport(CommentReport report);

    List<CommentReport> findAllReportsForUser(Long userId, CommentReportState state, Pageable pageable);

    CommentReport findReport(Long id);

    void resolveAllReportsToComment(Long commentId, CommentReportState commentReportState, LocalDateTime resolvedAt);
}
