package com.sssupply.customerportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferencesRequest {

    private boolean emailEnabled;
    private boolean inAppEnabled;

    private boolean notifyOnTicketUpdate;
    private boolean notifyOnComment;
    private boolean notifyOnAssignment;
    private boolean notifyOnSlaWarning;
    private boolean notifyOnSlaBreachAlert;
}
