package com.sssupply.customerportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentPerformanceResponse {
    private UUID agentId;
    private String agentName;
    private long assignedTickets;
    private long resolvedTickets;
    private BigDecimal avgResolutionHours;
    private BigDecimal slaCompliancePercent;
}
