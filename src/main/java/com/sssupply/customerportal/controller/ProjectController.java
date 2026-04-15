package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.*;
import com.sssupply.customerportal.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Projects")
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * GET /api/v1/projects
     * List workspace projects
     * Roles: All
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        return ResponseEntity.ok(ApiResponse.success(projectService.getAllProjects()));
    }

    /**
     * POST /api/v1/projects
     * Create project
     * Roles: 3sc_lead, 3sc_admin
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(ApiResponse.success(projectService.createProject(request)));
    }

    /**
     * GET /api/v1/projects/{id}
     * Project detail + milestones
     * Roles: All
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(projectService.getProjectById(id)));
    }

    /**
     * PATCH /api/v1/projects/{id}
     * Update project
     * Roles: 3sc_lead, 3sc_admin
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable UUID id,
            @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(ApiResponse.success(projectService.updateProject(id, request)));
    }

    /**
     * POST /api/v1/projects/{id}/milestones
     * Add milestone
     * Roles: 3sc_lead, 3sc_admin
     */
    @PostMapping("/{id}/milestones")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<MilestoneResponse>> addMilestone(
            @PathVariable UUID id,
            @Valid @RequestBody MilestoneRequest request) {
        return ResponseEntity.ok(ApiResponse.success(projectService.addMilestone(id, request)));
    }
}
