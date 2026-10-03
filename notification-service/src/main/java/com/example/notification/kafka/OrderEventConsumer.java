package com.example.notification.kafka;

import com.example.notification.model.OrderEvent;
import com.example.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final NotificationService notificationService;

    public OrderEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onOrderCreated(OrderEvent event) {
        log.info("Received order event {}", event.orderId());
        // If this throws, DefaultErrorHandler (KafkaConsumerConfig) retries, then routes to the DLT.
        // The offset is committed only after this method returns successfully.
        notificationService.send(event);
    }

    @KafkaListener(topics = "${app.kafka.topic}.DLT", groupId = "${spring.kafka.consumer.group-id}-dlt")
    public void onDeadLetter(OrderEvent event) {
        log.error("DEAD LETTER - gave up after retries: order {} ({})", event.orderId(), event.customerEmail());
    }
}
