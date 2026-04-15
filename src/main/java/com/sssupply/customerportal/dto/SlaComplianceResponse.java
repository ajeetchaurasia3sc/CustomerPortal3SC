package com.sssupply.customerportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlaComplianceResponse {
    private String priority;
    private long totalTickets;
    private long withinSla;
    private long breached;
    private BigDecimal compliancePercent;
}
