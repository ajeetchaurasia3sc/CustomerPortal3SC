package com.sssupply.customerportal.dto;

import com.sssupply.customerportal.enums.TicketCategory;
import com.sssupply.customerportal.enums.TicketPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiClassifyResponse {

    private TicketCategory suggestedCategory;
    private TicketPriority suggestedPriority;
    private List<String> suggestedTags;
    private BigDecimal confidence;
    private List<UUID> similarTicketIds;
}
