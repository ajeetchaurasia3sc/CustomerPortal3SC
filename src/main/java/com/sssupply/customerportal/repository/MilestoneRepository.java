package com.sssupply.customerportal.repository;
import com.sssupply.customerportal.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {
    List<Milestone> findByProjectIdAndDeletedAtIsNull(UUID projectId);
}
