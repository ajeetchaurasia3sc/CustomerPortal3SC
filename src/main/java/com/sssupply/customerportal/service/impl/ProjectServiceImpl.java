package com.sssupply.customerportal.service.impl;

import com.sssupply.customerportal.dto.MilestoneRequest;
import com.sssupply.customerportal.dto.MilestoneResponse;
import com.sssupply.customerportal.dto.ProjectRequest;
import com.sssupply.customerportal.dto.ProjectResponse;
import com.sssupply.customerportal.entity.Milestone;
import com.sssupply.customerportal.entity.Project;
import com.sssupply.customerportal.entity.User;
import com.sssupply.customerportal.enums.MilestoneStatus;
import com.sssupply.customerportal.enums.ProjectStatus;
import com.sssupply.customerportal.repository.MilestoneRepository;
import com.sssupply.customerportal.repository.ProjectRepository;
import com.sssupply.customerportal.repository.UserRepository;
import com.sssupply.customerportal.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MilestoneRepository milestoneRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        User currentUser = getCurrentUser();
        return projectRepository
                .findByWorkspaceIdAndDeletedAtIsNull(currentUser.getWorkspace().getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        User currentUser = getCurrentUser();

        Project project = Project.builder()
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : ProjectStatus.ACTIVE)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .workspace(currentUser.getWorkspace())
                .build();

        project = projectRepository.save(project);
        return mapToResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(UUID id) {
        Project project = findProjectById(id);
        return mapToResponseWithMilestones(project);
    }

    @Override
    public ProjectResponse updateProject(UUID id, ProjectRequest request) {
        Project project = findProjectById(id);

        if (request.getName() != null) project.setName(request.getName());
        if (request.getStatus() != null) project.setStatus(request.getStatus());
        if (request.getStartDate() != null) project.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) project.setEndDate(request.getEndDate());

        project = projectRepository.save(project);
        return mapToResponse(project);
    }

    @Override
    public MilestoneResponse addMilestone(UUID projectId, MilestoneRequest request) {
        Project project = findProjectById(projectId);

        Milestone milestone = Milestone.builder()
                .project(project)
                .title(request.getTitle())
                .dueDate(request.getDueDate())
                .status(request.getStatus() != null ? request.getStatus() : MilestoneStatus.NOT_STARTED)
                .build();

        milestone = milestoneRepository.save(milestone);
        return mapMilestoneToResponse(milestone);
    }

    @Override
    public MilestoneResponse updateMilestone(UUID milestoneId, MilestoneRequest request) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found with id: " + milestoneId));

        if (request.getTitle() != null) milestone.setTitle(request.getTitle());
        if (request.getStatus() != null) milestone.setStatus(request.getStatus());
        if (request.getDueDate() != null) milestone.setDueDate(request.getDueDate());

        milestone = milestoneRepository.save(milestone);
        return mapMilestoneToResponse(milestone);
    }

    // ==================== Helpers ====================

    private Project findProjectById(UUID id) {
        return projectRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    private ProjectResponse mapToResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .workspaceId(project.getWorkspace().getId())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    private ProjectResponse mapToResponseWithMilestones(Project project) {
        List<MilestoneResponse> milestones = milestoneRepository
                .findByProjectIdAndDeletedAtIsNull(project.getId())
                .stream()
                .map(this::mapMilestoneToResponse)
                .collect(Collectors.toList());

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .workspaceId(project.getWorkspace().getId())
                .milestones(milestones)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    private MilestoneResponse mapMilestoneToResponse(Milestone milestone) {
        return MilestoneResponse.builder()
                .id(milestone.getId())
                .projectId(milestone.getProject().getId())
                .title(milestone.getTitle())
                .dueDate(milestone.getDueDate())
                .status(milestone.getStatus())
                .createdAt(milestone.getCreatedAt())
                .updatedAt(milestone.getUpdatedAt())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
