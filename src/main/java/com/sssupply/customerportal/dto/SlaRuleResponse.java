package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlaRuleResponse {
    private UUID id;
    private String name;
    private TicketPriority priority;
    private TicketCategory category;
    private BigDecimal firstResponseHours;
    private BigDecimal resolutionHours;
    private Integer warnAtPercent;
    private boolean escalateOnBreach;
    private UUID escalateToUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
