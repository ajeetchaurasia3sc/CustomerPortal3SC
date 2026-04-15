package com.sssupply.customerportal.repository;

import com.sssupply.customerportal.entity.Project;
import com.sssupply.customerportal.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByWorkspaceIdAndDeletedAtIsNull(UUID workspaceId);

    List<Project> findByWorkspaceIdAndStatusAndDeletedAtIsNull(UUID workspaceId, ProjectStatus status);

    Optional<Project> findByIdAndDeletedAtIsNull(UUID id);
}
