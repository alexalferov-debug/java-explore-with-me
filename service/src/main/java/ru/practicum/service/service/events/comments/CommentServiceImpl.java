package ru.practicum.service.service.events.comments;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dao.events.EventDao;
import ru.practicum.service.dao.events.comment.CommentDao;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.data.EventState;
import ru.practicum.service.dto.event.comment.AddCommentDto;
import ru.practicum.service.dto.event.comment.FullCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDto;
import ru.practicum.service.dto.event.comment.ShortCommentDtoWithEvent;
import ru.practicum.service.dto.event.comment.report.NewReportDto;
import ru.practicum.service.dto.event.comment.report.ReportAddNote;
import ru.practicum.service.dto.event.comment.report.ReportFullInfoDto;
import ru.practicum.service.dto.event.comment.report.ReportInfoDto;
import ru.practicum.service.exception.DataConflictException;
import ru.practicum.service.exception.EventValidationException;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.mapper.CommentMapper;
import ru.practicum.service.model.*;
import ru.practicum.service.repository.ReportReasonRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    EventDao eventDao;
    UserDao userDao;
    CommentDao commentDao;
    ReportReasonRepository reasonRepository;

    public CommentServiceImpl(EventDao eventDao,
                              UserDao userDao,
                              CommentDao commentDao,
                              ReportReasonRepository reasonRepository) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.reasonRepository = reasonRepository;
    }

    @Override
    @Transactional
    public ShortCommentDto addComment(Long userId,
                                      Long eventId,
                                      HttpServletRequest requestData,
                                      AddCommentDto addCommentDto) {
        Event event = eventDao.findById(eventId);
        User user = userDao.get(userId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventValidationException("Комментарий можно добавить только к опубликованному событию");
        }
        Comment comment = new Comment();
        comment.setCommenterIp(requestData.getRemoteAddr());
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setContent(addCommentDto.getContent());
        return CommentMapper.INSTANCE.toShortCommentDto(commentDao.create(comment));
    }

    @Override
    @Transactional
    public ShortCommentDto addReply(Long userId, Long commentId, HttpServletRequest requestData, AddCommentDto addCommentDto) {
        User user = userDao.get(userId);
        Comment parent = commentDao.findById(commentId);
        Comment comment = new Comment();
        comment.setCommenterIp(requestData.getRemoteAddr());
        comment.setAuthor(user);
        comment.setEvent(parent.getEvent());
        comment.setContent(addCommentDto.getContent());
        comment.setParent(parent);
        Comment createdComment = commentDao.create(comment);
        return CommentMapper.INSTANCE.toShortCommentDto(createdComment);
    }

    @Override
    @Transactional
    public ShortCommentDto patchComment(Long userId,
                                        Long commentId,
                                        AddCommentDto addCommentDto) {
        User user = userDao.get(userId);
        Comment comment = commentDao.findById(commentId);
        if (comment.getIsDeleted()) {
            throw new NotFoundException("Не найден комментарий с id = " + commentId);
        }
        if (!comment.getAuthor().equals(user)) {
            throw new DataConflictException("Нельзя редактировать чужие комментарии");
        }
        comment.setContent(addCommentDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        return CommentMapper.INSTANCE.toShortCommentDto(commentDao.update(comment));
    }

    @Override
    @Transactional
    public void softDeleteComment(Long userId, Long commentId) {
        User user = userDao.get(userId);
        Comment comment = commentDao.findById(commentId);
        if (comment.getIsDeleted()) {
            throw new NotFoundException("Не найден комментарий с id = " + commentId);
        }
        if (!comment.getAuthor().equals(user)) {
            throw new DataConflictException("Нельзя удалять чужие комментарии");
        }
        comment.setIsDeleted(true);
        commentDao.update(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentDao.findById(commentId);
        commentDao.delete(commentId);
    }

    @Override
    public ShortCommentDto getComment(Long commentId) {
        return CommentMapper.INSTANCE.toShortCommentDto(commentDao.findNotDeletedById(commentId));
    }

    @Override
    public List<ShortCommentDto> getComments(Long eventId, int from, int size) {
        eventDao.findById(eventId);
        Pageable pageable = PageRequest.of(from / size,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentDao.findAllForEvent(eventId, pageable).stream().map(CommentMapper.INSTANCE::toShortCommentDto).toList();
    }

    @Override
    public List<ShortCommentDto> getReplies(Long commentId, int from, int size) {
        Comment comment = commentDao.findById(commentId);
        Pageable pageable = PageRequest.of(from / size,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        if (comment.getIsDeleted()) {
            throw new NotFoundException("Не найден комментарий с id = " + commentId);
        }
        if (!comment.getHasReplies()) {
            return List.of();
        }
        return commentDao.findAllReplies(commentId, pageable).stream().map(CommentMapper.INSTANCE::toShortCommentDto).toList();//поддержать изменение структуры
    }

    @Override
    public List<ShortCommentDtoWithEvent> getCommentsForUser(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentDao.findAllForUser(userId, pageable).stream().map(CommentMapper.INSTANCE::toShortCommentDtoWithEvent).toList();
    }

    @Override
    public List<FullCommentDto> getCommentsFromAdmin(List<Long> userIds,
                                                     List<Long> eventIds,
                                                     LocalDateTime createdFrom,
                                                     LocalDateTime createdTo,
                                                     Boolean isDeleted,
                                                     Boolean hasReplies,
                                                     String content,
                                                     int from,
                                                     int size) {
        Pageable pageable = PageRequest.of(from / size,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentDao.findCommentsForAdmin(userIds,
                eventIds,
                createdFrom,
                createdTo,
                isDeleted,
                hasReplies,
                content,
                pageable).stream().map(CommentMapper.INSTANCE::toFullCommentDto).toList();
    }

    @Override
    @Transactional
    public ReportInfoDto addReport(Long userId, Long commentId, NewReportDto reportInfoDto) {
        User user = userDao.get(userId);
        Comment comment = commentDao.findById(commentId);
        ReportReasons reason = reasonRepository.findById(reportInfoDto.getReasonId()).orElseThrow(() -> new NotFoundException("Причина обращения с id = " + reportInfoDto.getReasonId() + " не найдена."));
        if (comment.getIsDeleted()) {
            throw new NotFoundException("Не найден комментарий с id = " + commentId);
        }
        CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .reporter(user)
                .description(reportInfoDto.getDescription())
                .reason(reason)
                .status(CommentReportState.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        return CommentMapper.INSTANCE.toReportInfoDto(commentDao.saveReport(commentReport));
    }

    @Override
    @Transactional
    public ReportFullInfoDto addReportNote(Long reportId, ReportAddNote note) {
        CommentReport report = commentDao.findReport(reportId);
        report.setNotes(report.getNotes() + "\n" + note.getNotes());
        return CommentMapper.INSTANCE.toReportFullInfoDto(commentDao.updateReport(report));
    }

    @Override
    @Transactional
    public ReportFullInfoDto resolveReport(Long reportId, CommentReportState state) {
        CommentReport report = commentDao.findReport(reportId);
        if (report.getStatus().equals(CommentReportState.PENDING) && !(state.equals(CommentReportState.RESOLVED) || state.equals(CommentReportState.REJECTED))) {
            throw new DataConflictException("Из ожидания возможен переход только в решенные или отклоненные");
        }
        if (!report.getStatus().equals(CommentReportState.PENDING) && !state.equals(CommentReportState.PENDING)) {
            throw new DataConflictException("Из решенных возможен переход только в ожидание");
        }
        report.setStatus(state);
        if (!state.equals(CommentReportState.PENDING)) {
            LocalDateTime resolvedAt = LocalDateTime.now();
            report.setResolvedAt(resolvedAt);
            commentDao.resolveAllReportsToComment(report.getComment().getId(), state, resolvedAt);
        } else {
            report.setResolvedAt(null);
        }
        if (state.equals(CommentReportState.RESOLVED)) {
            Comment comment = report.getComment();
            comment.setIsDeleted(true);
            commentDao.update(comment);
        }
        return CommentMapper.INSTANCE.toReportFullInfoDto(commentDao.updateReport(report));
    }

    @Override
    public ReportInfoDto getReportInfo(Long userId, Long reportId) {
        User user = userDao.get(userId);
        CommentReport report = commentDao.findReport(reportId);
        if (user.getId().equals(report.getReporter().getId())) {
            throw new DataConflictException("Сообщение не найдена");
        }
        return null;
    }

    @Override
    public List<ReportInfoDto> getReportInfos(Long userId, CommentReportState state, int from, int size) {
        Pageable pageable = PageRequest.of(from / size,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentDao.findAllReportsForUser(userId, state, pageable).stream().map(CommentMapper.INSTANCE::toReportInfoDto).toList();
    }

    @Override
    public List<ReportFullInfoDto> getReportsForAdmin(List<Long> eventsId,
                                                      List<Long> reporterId,
                                                      List<Long> commenterId,
                                                      List<Long> commentsId,
                                                      List<Long> reason,
                                                      LocalDateTime createdFrom,
                                                      LocalDateTime createdTo,
                                                      List<CommentReportState> state,
                                                      int from,
                                                      int size) {
        Pageable pageable = PageRequest.of(from / size,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentDao.getReportsForAdmin(eventsId,
                reporterId,
                commenterId,
                commentsId,
                reason,
                createdFrom,
                createdTo,
                state,
                pageable).stream().map(CommentMapper.INSTANCE::toReportFullInfoDto).toList();
    }

    @Override
    public List<ReportReasons> getReportReasons() {
        return reasonRepository.findAll();
    }
}
