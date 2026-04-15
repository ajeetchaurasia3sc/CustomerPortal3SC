package com.sssupply.customerportal.service;

import com.sssupply.customerportal.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AnalyticsService {

    List<IssueTrendPoint> getIssueTrends(LocalDate from, LocalDate to, String groupBy);

    List<ResolutionTimeResponse> getResolutionTime(LocalDate from, LocalDate to);

    List<SlaComplianceResponse> getSlaCompliance(LocalDate from, LocalDate to);

    CsatResponse getCsat(LocalDate from, LocalDate to);

    List<AgentPerformanceResponse> getAgentPerformance(LocalDate from, LocalDate to);

    byte[] exportReport(AnalyticsExportRequest request);
}
