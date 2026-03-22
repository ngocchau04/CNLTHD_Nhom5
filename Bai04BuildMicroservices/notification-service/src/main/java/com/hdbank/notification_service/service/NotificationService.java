package com.hdbank.notification_service.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hdbank.notification_service.dto.OrderNotification;
import com.hdbank.notification_service.store.NotificationRecord;
import com.hdbank.notification_service.store.NotificationStore;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationStore store;

    public NotificationService(NotificationStore store) {
        this.store = store;
    }

    public void handleNotification(OrderNotification notification) {
        log.info("Notification received: orderNumber={}, message={}",
                notification.getOrderNumber(), notification.getMessage());

        store.add(new NotificationRecord(
                notification.getOrderNumber(),
                notification.getMessage(),
                Instant.now()
        ));

        simulateEmailSend(notification);
    }

    private void simulateEmailSend(OrderNotification notification) {
        log.info("Simulate email: Order {} -> {}",
                notification.getOrderNumber(), notification.getMessage());
    }
}
