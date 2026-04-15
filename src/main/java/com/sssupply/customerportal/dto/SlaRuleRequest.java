package com.sssupply.customerportal.dto;
import com.sssupply.customerportal.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlaRuleRequest {
    @NotBlank(message = "Name is required") private String name;
    @NotNull private TicketPriority priority;
    @NotNull private TicketCategory category;
    @NotNull private BigDecimal firstResponseHours;
    @NotNull private BigDecimal resolutionHours;
    @NotNull private Integer warnAtPercent;
    private boolean escalateOnBreach = true;
    private UUID escalateToUserId;
}
