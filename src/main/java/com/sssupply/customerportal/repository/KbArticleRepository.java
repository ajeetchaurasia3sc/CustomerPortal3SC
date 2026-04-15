package com.sssupply.customerportal.repository;

import com.sssupply.customerportal.entity.KbArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface KbArticleRepository extends JpaRepository<KbArticle, UUID> {

    List<KbArticle> findByWorkspaceIdOrWorkspaceIdIsNullAndPublishedTrueAndDeletedAtIsNull(UUID workspaceId);

    List<KbArticle> findByPublishedTrueAndDeletedAtIsNull();

    /**
     * Sum of helpful votes for CSAT analytics within a date range.
     * Falls back to all-time total when the article has no createdAt in range (safe default).
     */
    @Query("""
            SELECT COALESCE(SUM(a.helpfulCount), 0)
            FROM KbArticle a
            WHERE (a.workspace.id = :workspaceId OR a.workspace IS NULL)
              AND a.deletedAt IS NULL
              AND a.createdAt BETWEEN :from AND :to
            """)
    long sumHelpfulCountInRange(
            @Param("workspaceId") UUID workspaceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("""
            SELECT COALESCE(SUM(a.notHelpfulCount), 0)
            FROM KbArticle a
            WHERE (a.workspace.id = :workspaceId OR a.workspace IS NULL)
              AND a.deletedAt IS NULL
              AND a.createdAt BETWEEN :from AND :to
            """)
    long sumNotHelpfulCountInRange(
            @Param("workspaceId") UUID workspaceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
