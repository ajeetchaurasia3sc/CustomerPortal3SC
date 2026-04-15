package com.sssupply.customerportal.repository;
import com.sssupply.customerportal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByTicketIdAndDeletedAtIsNullOrderByCreatedAtAsc(UUID ticketId);
    List<Comment> findByTicketIdAndIsInternalFalseAndDeletedAtIsNullOrderByCreatedAtAsc(UUID ticketId);
    List<Comment> findByTicketIdAndIsInternalTrueAndDeletedAtIsNullOrderByCreatedAtAsc(UUID ticketId);
}
