package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.TicketStatus;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
@Data @Builder
public class AuditTrailResponse {
    private UUID id;
    private TicketStatus fromStatus;
    private TicketStatus toStatus;
    private UUID changedByUserId;
    private String note;
    private Instant createdAt;
}
