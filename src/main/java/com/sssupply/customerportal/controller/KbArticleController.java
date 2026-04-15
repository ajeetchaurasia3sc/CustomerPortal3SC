package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.ApiResponse;
import com.sssupply.customerportal.dto.KbArticleRequest;
import com.sssupply.customerportal.dto.KbArticleResponse;
import com.sssupply.customerportal.dto.KbFeedbackRequest;
import com.sssupply.customerportal.service.KbArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Knowledge Base")
@RestController
@RequestMapping("/api/v1/kb/articles")
@RequiredArgsConstructor
public class KbArticleController {

    private final KbArticleService kbArticleService;

    /**
     * GET /api/v1/kb/articles
     * List published articles (workspace-scoped for customers; all for staff)
     * Roles: All
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<KbArticleResponse>>> listArticles() {
        return ResponseEntity.ok(ApiResponse.success(kbArticleService.listArticles()));
    }

    /**
     * POST /api/v1/kb/articles
     * Create article
     * Roles: 3sc_*
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('INTERNAL_AGENT', 'INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<KbArticleResponse>> createArticle(
            @Valid @RequestBody KbArticleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(kbArticleService.createArticle(request)));
    }

    /**
     * GET /api/v1/kb/articles/{id}
     * Read article
     * Roles: All
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KbArticleResponse>> getArticle(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(kbArticleService.getArticleById(id)));
    }

    /**
     * PATCH /api/v1/kb/articles/{id}
     * Update article
     * Roles: 3sc_*
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('INTERNAL_AGENT', 'INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<KbArticleResponse>> updateArticle(
            @PathVariable UUID id,
            @RequestBody KbArticleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(kbArticleService.updateArticle(id, request)));
    }

    /**
     * DELETE /api/v1/kb/articles/{id}
     * Archive (soft-delete) article
     * Roles: 3sc_admin
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable UUID id) {
        kbArticleService.deleteArticle(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * POST /api/v1/kb/articles/{id}/feedback
     * Submit helpful / not-helpful feedback
     * Roles: All
     */
    @PostMapping("/{id}/feedback")
    public ResponseEntity<ApiResponse<KbArticleResponse>> submitFeedback(
            @PathVariable UUID id,
            @Valid @RequestBody KbFeedbackRequest request) {
        return ResponseEntity.ok(ApiResponse.success(kbArticleService.submitFeedback(id, request)));
    }
}
