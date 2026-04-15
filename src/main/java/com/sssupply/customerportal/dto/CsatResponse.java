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
public class CsatResponse {
    private BigDecimal averageScore;
    private long totalResponses;
    private long positiveCount;
    private long negativeCount;
    private BigDecimal satisfactionPercent;
}
