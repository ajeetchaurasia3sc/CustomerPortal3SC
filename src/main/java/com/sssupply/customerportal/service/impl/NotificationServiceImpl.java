package com.sssupply.customerportal.service.impl;

import com.sssupply.customerportal.dto.NotificationPreferencesRequest;
import com.sssupply.customerportal.dto.NotificationResponse;
import com.sssupply.customerportal.entity.Notification;
import com.sssupply.customerportal.entity.User;
import com.sssupply.customerportal.repository.NotificationRepository;
import com.sssupply.customerportal.repository.UserRepository;
import com.sssupply.customerportal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsForCurrentUser() {
        User currentUser = getCurrentUser();
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .filter(n -> n.getDeletedAt() == null)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponse markAsRead(UUID notificationId) {
        User currentUser = getCurrentUser();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied: this notification does not belong to you");
        }

        if (notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
            notification = notificationRepository.save(notification);
        }

        return mapToResponse(notification);
    }

    @Override
    public void markAllAsRead() {
        User currentUser = getCurrentUser();
        List<Notification> unread = notificationRepository
                .findByUserIdAndReadAtIsNullOrderByCreatedAtDesc(currentUser.getId());

        LocalDateTime now = LocalDateTime.now();
        unread.forEach(n -> n.setReadAt(now));
        notificationRepository.saveAll(unread);
    }

    @Override
    public String updatePreferences(NotificationPreferencesRequest request) {
        // TODO: Persist preferences to a user_notification_preferences table in a later iteration.
        // For now, acknowledge the request and return success.
        return "Notification preferences updated successfully.";
    }

    // ==================== Helpers ====================

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .body(notification.getBody())
                .entityType(notification.getEntityType())
                .entityId(notification.getEntityId())
                .read(notification.getReadAt() != null)
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
