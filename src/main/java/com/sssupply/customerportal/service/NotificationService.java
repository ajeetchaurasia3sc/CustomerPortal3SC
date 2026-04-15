package com.sssupply.customerportal.service;

import com.sssupply.customerportal.dto.NotificationPreferencesRequest;
import com.sssupply.customerportal.dto.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<NotificationResponse> getNotificationsForCurrentUser();

    NotificationResponse markAsRead(UUID notificationId);

    void markAllAsRead();

    String updatePreferences(NotificationPreferencesRequest request);
}
