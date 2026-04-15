package com.sssupply.customerportal.repository;
import com.sssupply.customerportal.entity.SlaRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface SlaRuleRepository extends JpaRepository<SlaRule, UUID> {
    List<SlaRule> findByWorkspaceIdOrWorkspaceIdIsNull(UUID workspaceId);
}
