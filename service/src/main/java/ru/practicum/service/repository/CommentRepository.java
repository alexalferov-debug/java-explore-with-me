package ru.practicum.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.service.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    Page<Comment> findAllByEventIdAndIsDeleted(Long eventId, Boolean isDeleted, Pageable pageable);

    Page<Comment> findAllByParentIdAndIsDeleted(Long parentId, Boolean isDeleted, Pageable pageable);

    Page<Comment> findAllByAuthorIdAndIsDeleted(Long authorId, Boolean isDeleted, Pageable pageable);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE (:users is null OR c.author.id in (:users)) " +
            "AND (:events is null OR c.event.id in (:events)) " +
            "AND c.createdAt >= COALESCE(:createdFrom, c.createdAt) " +
            "AND c.createdAt <= COALESCE(:createdTo, c.createdAt) " +
            "AND (LOWER(c.content) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:isDeleted is null or c.isDeleted = :isDeleted) " +
            "AND (:withReplies is null or c.hasReplies = :withReplies)")
    Page<Comment> findCommentsToAdmin(@Param("users") List<Long> userIds,
                                      @Param("events") List<Long> eventIds,
                                      @Param("createdFrom") LocalDateTime createdFrom,
                                      @Param("createdTo") LocalDateTime createdTo,
                                      @Param("isDeleted") Boolean isDeleted,
                                      @Param("withReplies") Boolean hasReplies,
                                      @Param("text") String content,
                                      Pageable pageable);
}
