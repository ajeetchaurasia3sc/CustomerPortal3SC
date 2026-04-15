package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.TicketStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TicketHistoryResponse {
    private UUID id;
    private TicketStatus fromStatus;
    private TicketStatus toStatus;
    private String changedByName;
    private String note;
    private LocalDateTime createdAt;
}
