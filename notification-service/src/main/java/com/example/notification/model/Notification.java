package com.example.notification.model;

import java.time.Instant;

public record Notification(String orderId, String to, String message, Instant sentAt) {
}
