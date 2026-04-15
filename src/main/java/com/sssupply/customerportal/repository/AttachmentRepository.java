package com.sssupply.customerportal.repository;
import com.sssupply.customerportal.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
    List<Attachment> findByTicketIdAndDeletedAtIsNull(UUID ticketId);
    List<Attachment> findByCommentIdAndDeletedAtIsNull(UUID commentId);
}
