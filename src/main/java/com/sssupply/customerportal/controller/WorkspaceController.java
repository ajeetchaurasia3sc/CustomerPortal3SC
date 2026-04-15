package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.WorkspaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Workspaces")
@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * GET /api/v1/workspaces
     * List all workspaces
     * Roles: 3sc_admin
     */
    @GetMapping
    @PreAuthorize("hasRole('INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<List<WorkspaceResponse>>> listWorkspaces() {
        return ResponseEntity.ok(ApiResponse.success(workspaceService.listAllWorkspaces()));
    }

    /**
     * POST /api/v1/workspaces
     * Create customer workspace
     * Roles: 3sc_admin
     */
    @PostMapping
    @PreAuthorize("hasRole('INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> createWorkspace(
            @Valid @RequestBody WorkspaceRequest request) {
        return ResponseEntity.ok(ApiResponse.success(workspaceService.createWorkspace(request)));
    }

    /**
     * GET /api/v1/workspaces/{id}
     * Get workspace detail
     * Roles: workspace members
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> getWorkspace(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(workspaceService.getWorkspaceById(id)));
    }

    /**
     * PATCH /api/v1/workspaces/{id}/branding
     * Update logo and brand colours
     * Roles: customer_admin, 3sc_admin
     */
    @PatchMapping("/{id}/branding")
    @PreAuthorize("hasAnyRole('CUSTOMER_ADMIN', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> updateBranding(
            @PathVariable UUID id,
            @RequestBody BrandingRequest request) {
        return ResponseEntity.ok(ApiResponse.success(workspaceService.updateBranding(id, request)));
    }

    /**
     * GET /api/v1/workspaces/{id}/members
     * List members
     * Roles: customer_admin, 3sc_*
     */
    @GetMapping("/{id}/members")
    @PreAuthorize("hasAnyRole('CUSTOMER_ADMIN', 'INTERNAL_AGENT', 'INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<List<WorkspaceMemberResponse>>> listMembers(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(workspaceService.listMembers(id)));
    }

    /**
     * DELETE /api/v1/workspaces/{id}/members/{userId}
     * Remove member
     * Roles: customer_admin, 3sc_admin
     */
    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER_ADMIN', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable UUID id,
            @PathVariable UUID userId) {
        workspaceService.removeMember(id, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
