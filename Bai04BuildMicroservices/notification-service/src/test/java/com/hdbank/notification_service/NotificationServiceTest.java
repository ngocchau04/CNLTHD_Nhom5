package com.hdbank.notification_service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdbank.notification_service.dto.OrderNotification;
import com.hdbank.notification_service.service.NotificationService;
import com.hdbank.notification_service.store.NotificationRecord;
import com.hdbank.notification_service.store.NotificationStore;

class NotificationServiceTest {

    @Test
    void handleNotification_shouldStoreRecord() {
        NotificationStore store = new NotificationStore();
        NotificationService service = new NotificationService(store);

        service.handleNotification(new OrderNotification("ORD123", "Order Placed Successfully"));

        List<NotificationRecord> records = store.getAll();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).orderNumber()).isEqualTo("ORD123");
        assertThat(records.get(0).message()).isEqualTo("Order Placed Successfully");
        assertThat(records.get(0).receivedAt()).isNotNull();
    }
}
