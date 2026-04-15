package com.sssupply.customerportal.service;

import com.sssupply.customerportal.dto.*;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {

    List<WorkspaceResponse> listAllWorkspaces();

    WorkspaceResponse createWorkspace(WorkspaceRequest request);

    WorkspaceResponse getWorkspaceById(UUID id);

    WorkspaceResponse updateBranding(UUID id, BrandingRequest request);

    List<WorkspaceMemberResponse> listMembers(UUID workspaceId);

    WorkspaceMemberResponse updateMemberRole(UUID memberId, UpdateMemberRoleRequest request);

    void removeMember(UUID workspaceId, UUID userId);
}
