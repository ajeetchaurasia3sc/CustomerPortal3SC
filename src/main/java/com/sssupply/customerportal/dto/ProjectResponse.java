package com.sssupply.customerportal.dto;

import com.sssupply.customerportal.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private UUID id;
    private String name;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID workspaceId;
    private List<MilestoneResponse> milestones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
