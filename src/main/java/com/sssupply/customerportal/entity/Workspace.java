package com.sssupply.customerportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workspaces")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workspace extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    /** Primary brand colour (hex, e.g. #2563EB) */
    @Column(name = "primary_color", length = 7)
    private String primaryColor;

    /** Accent / secondary colour */
    @Column(name = "accent_color", length = 7)
    private String accentColor;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "custom_domain")
    private String customDomain;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
