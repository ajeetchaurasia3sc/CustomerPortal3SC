package com.sssupply.customerportal.dto;

import com.sssupply.customerportal.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private UUID id;
    private NotificationType type;
    private String title;
    private String body;
    private String entityType;
    private UUID entityId;
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
}
