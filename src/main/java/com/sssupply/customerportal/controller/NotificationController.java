package com.sssupply.customerportal.controller;

import com.sssupply.customerportal.dto.ApiResponse;
import com.sssupply.customerportal.dto.NotificationPreferencesRequest;
import com.sssupply.customerportal.dto.NotificationResponse;
import com.sssupply.customerportal.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notifications")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * GET /api/v1/notifications
     * List user notifications (all, newest first)
     * Roles: All
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications() {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getNotificationsForCurrentUser()));
    }

    /**
     * PATCH /api/v1/notifications/{id}/read
     * Mark a single notification as read
     * Roles: Owner
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.markAsRead(id)));
    }

    /**
     * PATCH /api/v1/notifications/read-all
     * Mark all notifications as read
     * Roles: All
     */
    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * PATCH /api/v1/notifications/preferences
     * Update email / in-app notification preferences
     * Roles: All
     */
    @PatchMapping("/preferences")
    public ResponseEntity<ApiResponse<String>> updatePreferences(
            @RequestBody NotificationPreferencesRequest request) {
        String message = notificationService.updatePreferences(request);
        return ResponseEntity.ok(ApiResponse.success(message));
    }
}
