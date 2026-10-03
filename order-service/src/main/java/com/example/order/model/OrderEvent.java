package com.example.order.model;

import java.math.BigDecimal;
import java.time.Instant;

/** The event published to Kafka whenever an order is created. */
public record OrderEvent(
        String orderId,
        String customerName,
        String customerEmail,
        String product,
        int quantity,
        BigDecimal totalAmount,
        Instant createdAt) {
}
