package ru.practicum.service.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.service.data.CommentReportState;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment_reports",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "comment_id"})
        })
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "reason", nullable = false)
    private ReportReasons reason;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentReportState status = CommentReportState.PENDING;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    private String notes;
}