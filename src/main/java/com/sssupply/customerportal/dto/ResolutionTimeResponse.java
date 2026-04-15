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
public class ResolutionTimeResponse {
    private String priority;
    private String category;
    private BigDecimal avgHours;
    private long ticketCount;
}
