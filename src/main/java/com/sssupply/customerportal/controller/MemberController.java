package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.ApiResponse;
import com.sssupply.customerportal.dto.UpdateMemberRoleRequest;
import com.sssupply.customerportal.dto.WorkspaceMemberResponse;
import com.sssupply.customerportal.service.WorkspaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Members")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final WorkspaceService workspaceService;

    /**
     * PATCH /api/v1/members/{id}/role
     * Update member role
     * Roles: customer_admin, 3sc_admin
     */
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAnyRole('CUSTOMER_ADMIN', 'INTERNAL_ADMIN')")
    public ResponseEntity<ApiResponse<WorkspaceMemberResponse>> updateMemberRole(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMemberRoleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(workspaceService.updateMemberRole(id, request)));
    }
}
