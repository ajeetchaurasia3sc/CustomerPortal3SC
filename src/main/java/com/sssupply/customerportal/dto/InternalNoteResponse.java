package com.sssupply.customerportal.dto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class InternalNoteResponse {
    private UUID id;
    private UUID ticketId;
    private String authorName;
    private String body;
    private LocalDateTime createdAt;
}
