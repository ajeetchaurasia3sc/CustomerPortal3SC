package com.sssupply.customerportal.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TicketAssignRequest {
    @NotNull private UUID agentId;
}
