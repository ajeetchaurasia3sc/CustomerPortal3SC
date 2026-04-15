package com.sssupply.customerportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsExportRequest {

    /** Report type: issue-trends | resolution-time | sla | csat | agent-performance */
    @NotBlank(message = "reportType is required")
    private String reportType;

    /** Format: csv | pdf */
    @NotBlank(message = "format is required (csv or pdf)")
    private String format;

    @NotNull(message = "from date is required")
    private LocalDate from;

    @NotNull(message = "to date is required")
    private LocalDate to;
}
