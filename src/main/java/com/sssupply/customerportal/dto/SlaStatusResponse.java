package com.sssupply.customerportal.dto;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlaStatusResponse {
    private boolean breached;
    private LocalDateTime slaDueAt;
    private Long hoursRemaining;
    private String status;
}
