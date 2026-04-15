package com.sssupply.customerportal.entity;

import com.sssupply.customerportal.enums.TicketCategory;
import com.sssupply.customerportal.enums.TicketPriority;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "sla_rules")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlaRule extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    private TicketCategory category;

    @Column(name = "first_response_hours", nullable = false, precision = 5, scale = 2)
    private BigDecimal firstResponseHours;

    @Column(name = "resolution_hours", nullable = false, precision = 5, scale = 2)
    private BigDecimal resolutionHours;

    @Column(name = "warn_at_percent", nullable = false)
    private Integer warnAtPercent;

    @Column(name = "escalate_on_breach", nullable = false)
    private boolean escalateOnBreach = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escalate_to_user_id")
    private User escalateToUser;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
