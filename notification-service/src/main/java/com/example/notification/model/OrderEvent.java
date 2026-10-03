package com.example.notification.model;

import java.math.BigDecimal;
import java.time.Instant;

/** Own copy of the event contract - the two services share no code, only the JSON shape. */
public record OrderEvent(
        String orderId,
        String customerName,
        String customerEmail,
        String product,
        int quantity,
        BigDecimal totalAmount,
        Instant createdAt) {
}
