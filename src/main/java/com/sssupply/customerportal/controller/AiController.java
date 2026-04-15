package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.AiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "AI")
@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * POST /api/v1/ai/classify
     * Auto-classify ticket — also called internally on ticket creation
     * Roles: All (triggered automatically, but also callable directly)
     */
    @PostMapping("/classify")
    public ResponseEntity<ApiResponse<AiClassifyResponse>> classify(
            @Valid @RequestBody AiClassifyRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiService.classifyTicket(request)));
    }

    /**
     * POST /api/v1/ai/suggest-response
     * Suggest agent reply from resolved similar tickets
     * Roles: 3sc_*
     */
    @PostMapping("/suggest-response")
    @PreAuthorize("hasAnyRole('INTERNAL_AGENT', 'INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<AiTextResponse>> suggestResponse(
            @Valid @RequestBody AiSuggestResponseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiService.suggestResponse(request)));
    }

    /**
     * POST /api/v1/ai/summarize-thread
     * Summarise conversation thread
     * Roles: All
     */
    @PostMapping("/summarize-thread")
    public ResponseEntity<ApiResponse<AiTextResponse>> summarizeThread(
            @Valid @RequestBody AiTextRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiService.summarizeThread(request.getTicketId())));
    }

    /**
     * POST /api/v1/ai/summarize-ticket
     * Full ticket TL;DR
     * Roles: All
     */
    @PostMapping("/summarize-ticket")
    public ResponseEntity<ApiResponse<AiTextResponse>> summarizeTicket(
            @Valid @RequestBody AiTextRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiService.summarizeTicket(request.getTicketId())));
    }

    /**
     * POST /api/v1/ai/search
     * Natural language ticket search
     * Roles: All
     */
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> search(
            @Valid @RequestBody AiSearchRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiService.search(request)));
    }

    /**
     * POST /api/v1/ai/auto-route
     * Suggest best agent based on history
     * Roles: 3sc_lead, 3sc_admin
     */
    @PostMapping("/auto-route")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<AiTextResponse>> autoRoute(
            @Valid @RequestBody AiTextRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiService.autoRoute(request.getTicketId())));
    }

    /**
     * POST /api/v1/ai/predict-resolution-time
     * Estimate resolution hours
     * Roles: 3sc_*
     */
    @PostMapping("/predict-resolution-time")
    @PreAuthorize("hasAnyRole('INTERNAL_AGENT', 'INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<AiPredictResolutionResponse>> predictResolutionTime(
            @Valid @RequestBody AiTextRequest request) {
        return ResponseEntity.ok(ApiResponse.success(aiService.predictResolutionTime(request.getTicketId())));
    }

    /**
     * GET /api/v1/ai/kb-search
     * AI-powered KB article search
     * Roles: All
     */
    @GetMapping("/kb-search")
    public ResponseEntity<ApiResponse<List<KbArticleResponse>>> kbSearch(
            @RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.success(aiService.kbSearch(query)));
    }

    /**
     * POST /api/v1/ai/generate-report-summary
     * Weekly/monthly project summary
     * Roles: 3sc_lead, 3sc_admin
     */
    @PostMapping("/generate-report-summary")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<AiTextResponse>> generateReportSummary(
            @RequestParam(required = false) String period) {
        return ResponseEntity.ok(ApiResponse.success(aiService.generateReportSummary(period)));
    }
}
