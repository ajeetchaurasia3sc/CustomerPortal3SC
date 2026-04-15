package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.AnalyticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Analytics")
@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * GET /api/v1/analytics/issue-trends
     * Roles: 3sc_lead, 3sc_admin, customer_admin
     */
    @GetMapping("/issue-trends")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN', 'CUSTOMER_ADMIN')")
    public ResponseEntity<ApiResponse<List<IssueTrendPoint>>> getIssueTrends(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "day") String groupBy) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getIssueTrends(from, to, groupBy)));
    }

    /**
     * GET /api/v1/analytics/resolution-time
     * Roles: 3sc_lead, 3sc_admin, customer_admin
     */
    @GetMapping("/resolution-time")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN', 'CUSTOMER_ADMIN')")
    public ResponseEntity<ApiResponse<List<ResolutionTimeResponse>>> getResolutionTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getResolutionTime(from, to)));
    }

    /**
     * GET /api/v1/analytics/sla
     * Roles: 3sc_*
     */
    @GetMapping("/sla")
    @PreAuthorize("hasAnyRole('INTERNAL_AGENT', 'INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<List<SlaComplianceResponse>>> getSlaCompliance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getSlaCompliance(from, to)));
    }

    /**
     * GET /api/v1/analytics/csat
     * Roles: 3sc_*
     */
    @GetMapping("/csat")
    @PreAuthorize("hasAnyRole('INTERNAL_AGENT', 'INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<CsatResponse>> getCsat(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getCsat(from, to)));
    }

    /**
     * GET /api/v1/analytics/agent-performance
     * Roles: 3sc_lead, 3sc_admin
     */
    @GetMapping("/agent-performance")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<List<AgentPerformanceResponse>>> getAgentPerformance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getAgentPerformance(from, to)));
    }

    /**
     * POST /api/v1/analytics/export
     * Export report as CSV (PDF coming soon)
     * Roles: 3sc_lead, 3sc_admin, customer_admin
     */
    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN', 'CUSTOMER_ADMIN')")
    public ResponseEntity<byte[]> exportReport(@Valid @RequestBody AnalyticsExportRequest request) {
        byte[] data = analyticsService.exportReport(request);
        String filename = request.getReportType() + "_" + request.getFrom() + "_to_" + request.getTo() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }
}
