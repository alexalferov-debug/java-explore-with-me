package ru.practicum.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.service.data.CommentReportState;
import ru.practicum.service.model.CommentReport;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    Page<CommentReport> findAllByReporterIdAndStatus(Long reporterId, CommentReportState reportState, Pageable pageable);

    @Modifying
    @Query("UPDATE CommentReport cr " +
            "SET cr.status = :status, cr.resolvedAt = :resolvedAt " +
            "WHERE cr.comment.id = :commentId")
    void updateStatus(@Param("commentId") Long commentId,
                      @Param("status") CommentReportState reportState,
                      @Param("resolvedAt") LocalDateTime resolvedAt);

    @Query(" SELECT cr " +
            "FROM CommentReport cr " +
            "WHERE (:eventsId IS NULL OR cr.comment.event.id in (:eventsId)) " +
            "AND (:reporterId IS NULL OR cr.reporter.id in (:reporterId)) " +
            "AND (:commenterId  IS NULL OR cr.comment.author.id in (:commenterId)) " +
            "AND (:commentsId  IS NULL OR cr.comment.id in (:commentsId)) " +
            "AND (:reason  IS NULL OR cr.reason.id in (:reason)) " +
            "AND (:state  IS NULL OR cr.status in (:state)) " +
            "AND cr.createdAt >= COALESCE(:createdFrom, cr.createdAt) " +
            "AND cr.createdAt <= COALESCE(:createdTo, cr.createdAt) ")
    Page<CommentReport> findAllForAdmin(@Param("eventsId") List<Long> eventsId,
                                        @Param("reporterId") List<Long> reporterId,
                                        @Param("commenterId") List<Long> commenterId,
                                        @Param("commentsId") List<Long> commentsId,
                                        @Param("createdFrom") LocalDateTime createdFrom,
                                        @Param("createdTo") LocalDateTime createdTo,
                                        @Param("reason") List<Long> reason,
                                        @Param("state") List<CommentReportState> state,
                                        Pageable pageable);


}
