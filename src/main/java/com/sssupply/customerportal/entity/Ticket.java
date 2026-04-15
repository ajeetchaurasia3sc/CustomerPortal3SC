package com.sssupply.customerportal.entity;

import com.sssupply.customerportal.enums.TicketCategory;
import com.sssupply.customerportal.enums.TicketPriority;
import com.sssupply.customerportal.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Column(name = "ticket_number")
    private Integer ticketNumber;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    // AI Suggested Fields
    @Enumerated(EnumType.STRING)
    private TicketCategory aiCategory;

    @Enumerated(EnumType.STRING)
    private TicketPriority aiPriority;

    @Column(name = "ai_confidence", precision = 5, scale = 4)
    private BigDecimal aiConfidence;

    @Column(name = "ai_predicted_hours", precision = 6, scale = 2)
    private BigDecimal aiPredictedHours;

    // SLA Fields
    @Column(name = "sla_due_at")
    private LocalDateTime slaDueAt;

    @Column(name = "sla_breached_at")
    private LocalDateTime slaBreachedAt;

    /**
     * Computed flag: true when slaBreachedAt is set.
     * Used by analytics queries for SLA compliance reporting.
     */
    @Column(name = "sla_breached", nullable = false)
    private boolean slaBreached = false;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "csat_score")
    private Integer csatScore;

    @Column(name = "tags")
    private String[] tags;

    @Column(name = "search_vector", columnDefinition = "tsvector", insertable = false, updatable = false)
    private Object searchVector;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
