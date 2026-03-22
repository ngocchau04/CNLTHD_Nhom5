package com.hdbank.notification_service.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

@Component
public class NotificationStore {
    private final List<NotificationRecord> records = new CopyOnWriteArrayList<>();

    public void add(NotificationRecord record) {
        records.add(record);
    }

    public List<NotificationRecord> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(records));
    }
}
