package com.sssupply.customerportal.service;

import com.sssupply.customerportal.dto.MilestoneRequest;
import com.sssupply.customerportal.dto.MilestoneResponse;
import com.sssupply.customerportal.dto.ProjectRequest;
import com.sssupply.customerportal.dto.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    List<ProjectResponse> getAllProjects();

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse getProjectById(UUID id);

    ProjectResponse updateProject(UUID id, ProjectRequest request);

    MilestoneResponse addMilestone(UUID projectId, MilestoneRequest request);

    MilestoneResponse updateMilestone(UUID milestoneId, MilestoneRequest request);
}
