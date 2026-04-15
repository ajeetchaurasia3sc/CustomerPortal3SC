package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.*;
import lombok.Data;
import java.util.UUID;
@Data
public class EscalationRuleRequest {
    private String name;
    private TicketPriority priority;
    private TicketCategory category;
    private Integer escalateAfterMinutes;
    private UUID escalateToUserId;
}
