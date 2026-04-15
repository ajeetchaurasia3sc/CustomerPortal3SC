package com.sssupply.customerportal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiSuggestResponseRequest {

    @NotNull(message = "ticketId is required")
    private UUID ticketId;
}
