package com.hdbank.notification_service.store;

import java.time.Instant;

public record NotificationRecord(String orderNumber, String message, Instant receivedAt) {
}
