package com.sssupply.customerportal.dto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttachmentResponse {
    private UUID id;
    private String filename;
    private String mimeType;
    private Long sizeBytes;
    private String uploadedByName;
    private LocalDateTime createdAt;
}
