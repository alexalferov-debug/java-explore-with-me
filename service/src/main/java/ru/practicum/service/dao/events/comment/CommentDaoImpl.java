package ru.practicum.service.dao.events.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.model.Comment;
import ru.practicum.service.model.CommentReport;
import ru.practicum.service.repository.CommentReportRepository;
import ru.practicum.service.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class CommentDaoImpl implements CommentDao {
    CommentRepository repository;
    CommentReportRepository reportRepository;

    public CommentDaoImpl(CommentRepository repository,
                          CommentReportRepository reportRepository) {
        this.repository = repository;
        this.reportRepository = reportRepository;
    }

    @Override
    public Comment create(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Comment update(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public Comment findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Не найден комментарий с id = " + id));
    }

    @Override
    public Comment findNotDeletedById(Long id) {
        return repository.findByIdAndIsDeleted(id, false).orElseThrow(() -> new NotFoundException("Не найден комментарий с id = " + id));
    }

    @Override
    public List<Comment> findAllForEvent(Long eventId, Pageable pageable) {
        return repository.findAllByEventIdAndIsDeleted(eventId, false, pageable).getContent();
    }

    @Override
    public List<Comment> findAllForUser(Long userId, Pageable pageable) {
        return repository.findAllByAuthorIdAndIsDeleted(userId, false, pageable).getContent();
    }

    @Override
    public List<Comment> findAllReplies(Long commentId, Pageable pageable) {
        return repository.findAllByParentIdAndIsDeleted(commentId, false, pageable).getContent();
    }

    @Override
    public List<Comment> findCommentsForAdmin(List<Long> userIds,
                                              List<Long> eventIds,
                                              LocalDateTime createdFrom,
                                              LocalDateTime createdTo,
                                              Boolean isDeleted,
                                              Boolean hasReplies,
                                              String content,
                                              Pageable pageable) {
        if (Objects.isNull(content)) {
            content = "";
        }
        return repository.findCommentsToAdmin(userIds,
                eventIds,
                createdFrom,
                createdTo,
                isDeleted,
                hasReplies,
                content,
                pageable).getContent();
    }

    @Override
    public CommentReport saveReport(CommentReport report) {
        return reportRepository.save(report);
    }

    @Override
    public CommentReport updateReport(CommentReport report) {
        return reportRepository.save(report);
    }

    @Override
    public List<CommentReport> findAllReportsForUser(Long userId, CommentReportState state, Pageable pageable) {
        return reportRepository.findAllByReporterIdAndStatus(userId, state, pageable).getContent();
    }

    @Override
    public CommentReport findReport(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найдена жалоба с id = " + id));
    }

    @Override
    public void resolveAllReportsToComment(Long commentId, CommentReportState commentReportState, LocalDateTime resolvedAt) {
        reportRepository.updateStatus(commentId, commentReportState, resolvedAt);
    }

    @Override
    public List<CommentReport> getReportsForAdmin(List<Long> eventsId,
                                                  List<Long> reporterId,
                                                  List<Long> commenterId,
                                                  List<Long> commentsId,
                                                  List<Long> reason,
                                                  LocalDateTime createdFrom,
                                                  LocalDateTime createdTo,
                                                  List<CommentReportState> state,
                                                  Pageable pageable) {
        return reportRepository.findAllForAdmin(eventsId,
                reporterId,
                commenterId,
                commentsId,
                createdFrom,
                createdTo,
                reason,
                state,
                pageable).getContent();
    }
}
