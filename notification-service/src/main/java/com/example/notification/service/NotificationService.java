package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    // In-memory store (use a DB in production). Key = orderId, which also gives idempotency.
    private final Map<String, Notification> sent = new ConcurrentHashMap<>();

    public void send(OrderEvent event) {
        // Kafka is at-least-once: the same event may arrive twice -> skip duplicates.
        if (sent.containsKey(event.orderId())) {
            log.warn("Duplicate event for order {} ignored", event.orderId());
            return;
        }

        // Demo hook: simulates a downstream failure to show retries + dead-letter topic.
        if (event.customerEmail().contains("fail")) {
            throw new IllegalStateException("Simulated email provider failure for " + event.customerEmail());
        }

        String msg = "Hi %s, your order for %d x %s (total %s) is confirmed."
                .formatted(event.customerName(), event.quantity(), event.product(), event.totalAmount());

        // A real implementation would call an email/SMS provider here.
        log.info("NOTIFICATION -> {} : {}", event.customerEmail(), msg);
        sent.put(event.orderId(), new Notification(event.orderId(), event.customerEmail(), msg, Instant.now()));
    }

    public List<Notification> all() {
        return List.copyOf(sent.values());
    }
}
