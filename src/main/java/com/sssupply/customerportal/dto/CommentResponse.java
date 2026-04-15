package com.sssupply.customerportal.dto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CommentResponse {
    private UUID id;
    private UUID ticketId;
    private String authorName;
    private String authorRole;
    private String body;
    private boolean isInternal;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private boolean isEdited;
    private boolean canEdit;
}
