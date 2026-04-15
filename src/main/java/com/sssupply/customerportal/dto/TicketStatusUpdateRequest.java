package com.sssupply.customerportal.dto;

import com.sssupply.customerportal.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TicketStatusUpdateRequest {
    @NotNull(message = "status is required")
    private TicketStatus status;
}
