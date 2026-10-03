package com.example.order.controller;

import com.example.order.kafka.OrderEventProducer;
import com.example.order.model.OrderEvent;
import com.example.order.model.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderEventProducer producer;

    public OrderController(OrderEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@Valid @RequestBody OrderRequest request) {
        String orderId = UUID.randomUUID().toString();

        OrderEvent event = new OrderEvent(
                orderId,
                request.customerName(),
                request.customerEmail(),
                request.product(),
                request.quantity(),
                request.price().multiply(BigDecimal.valueOf(request.quantity())),
                Instant.now());

        producer.publish(event);

        // 202 Accepted: order created, notification is handled asynchronously
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("orderId", orderId, "status", "CREATED"));
    }
}
