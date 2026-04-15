package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.*;
import lombok.*;
import java.util.UUID;
@Data @Builder
public class EscalationRuleResponse {
    private UUID id;
    private String name;
    private TicketPriority priority;
    private TicketCategory category;
    private Integer escalateAfterMinutes;
    private UUID escalateToUserId;
}
