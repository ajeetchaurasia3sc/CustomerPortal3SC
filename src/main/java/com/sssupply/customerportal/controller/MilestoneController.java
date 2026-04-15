package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.ApiResponse;
import com.sssupply.customerportal.dto.MilestoneRequest;
import com.sssupply.customerportal.dto.MilestoneResponse;
import com.sssupply.customerportal.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Milestones")
@RestController
@RequestMapping("/api/v1/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final ProjectService projectService;

    /**
     * PATCH /api/v1/milestones/{id}
     * Update milestone status or date
     * Roles: 3sc_lead, 3sc_admin
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('INTERNAL_LEAD', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<MilestoneResponse>> updateMilestone(
            @PathVariable UUID id,
            @RequestBody MilestoneRequest request) {
        return ResponseEntity.ok(ApiResponse.success(projectService.updateMilestone(id, request)));
    }
}
