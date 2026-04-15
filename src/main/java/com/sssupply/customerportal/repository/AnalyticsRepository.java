package com.sssupply.customerportal.repository;

import com.sssupply.customerportal.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsRepository extends JpaRepository<Ticket, UUID> {

    /**
     * Returns rows of [date(truncated to day), status, category, count].
     * Uses Native Query to ensure DATE_TRUNC is handled correctly by Postgres.
     */
    @Query(value = """
            SELECT DATE_TRUNC('day', t.created_at) as day, t.status, t.category, COUNT(*)
            FROM tickets t
            WHERE t.workspace_id = :workspaceId
              AND t.created_at BETWEEN :from AND :to
              AND t.deleted_at IS NULL
            GROUP BY DATE_TRUNC('day', t.created_at), t.status, t.category
            ORDER BY day
            """, nativeQuery = true)
    List<Object[]> findIssueTrends(
            @Param("workspaceId") UUID workspaceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    /**
     * Avg resolution time (hours) grouped by priority and category.
     * Uses Native Query to fix the Hibernate 'EXTRACT EPOCH' validation error.
     */
    @Query(value = """
            SELECT t.priority, t.category,
                   AVG(EXTRACT(EPOCH FROM (t.resolved_at - t.created_at))) / 3600.0,
                   COUNT(*)
            FROM tickets t
            WHERE t.workspace_id = :workspaceId
              AND t.resolved_at IS NOT NULL
              AND t.created_at BETWEEN :from AND :to
              AND t.deleted_at IS NULL
            GROUP BY t.priority, t.category
            """, nativeQuery = true)
    List<Object[]> findResolutionTime(
            @Param("workspaceId") UUID workspaceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    /**
     * SLA compliance: counts total, SLA-met, and breached tickets per priority.
     * Switched to Native SQL for consistency with other methods.
     */
    @Query(value = """
            SELECT t.priority,
                   COUNT(*),
                   SUM(CASE WHEN t.sla_breached = false THEN 1 ELSE 0 END),
                   SUM(CASE WHEN t.sla_breached = true  THEN 1 ELSE 0 END)
            FROM tickets t
            WHERE t.workspace_id = :workspaceId
              AND t.created_at BETWEEN :from AND :to
              AND t.deleted_at IS NULL
            GROUP BY t.priority
            """, nativeQuery = true)
    List<Object[]> findSlaCompliance(
            @Param("workspaceId") UUID workspaceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    /**
     * Per-agent metrics: assigned count, resolved count, avg resolution hours.
     * Uses a JOIN on the users table.
     */
    @Query(value = """
            SELECT u.id, u.name,
                   COUNT(t.id),
                   SUM(CASE WHEN t.resolved_at IS NOT NULL THEN 1 ELSE 0 END),
                   AVG(CASE WHEN t.resolved_at IS NOT NULL 
                       THEN EXTRACT(EPOCH FROM (t.resolved_at - t.created_at)) / 3600.0 
                       ELSE NULL END),
                   AVG(CASE WHEN t.sla_breached = false THEN 100.0 ELSE 0.0 END)
            FROM tickets t
            JOIN users u ON t.assigned_to_id = u.id
            WHERE t.workspace_id = :workspaceId
              AND t.assigned_to_id IS NOT NULL
              AND t.created_at BETWEEN :from AND :to
              AND t.deleted_at IS NULL
            GROUP BY u.id, u.name
            """, nativeQuery = true)
    List<Object[]> findAgentPerformance(
            @Param("workspaceId") UUID workspaceId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}