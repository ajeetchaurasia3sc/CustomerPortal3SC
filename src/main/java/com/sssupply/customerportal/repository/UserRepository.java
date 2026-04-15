package com.sssupply.customerportal.repository;

import com.sssupply.customerportal.entity.User;
import com.sssupply.customerportal.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    boolean existsByEmailAndDeletedAtIsNull(String email);

    List<User> findByWorkspaceIdAndDeletedAtIsNull(UUID workspaceId);

    int countByWorkspaceIdAndDeletedAtIsNull(UUID workspaceId);

    List<User> findByRoleAndDeletedAtIsNull(UserRole role);

    Optional<User> findByInviteTokenAndDeletedAtIsNull(UUID inviteToken);
}
