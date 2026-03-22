package com.hdbank.notification_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.hdbank.notification_service.dto.OrderNotification;
import com.hdbank.notification_service.service.NotificationService;

@Component
public class NotificationListener {
    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "notificationTopic")
    public void onMessage(OrderNotification notification) {
        notificationService.handleNotification(notification);
    }
}
