# Order-Notification Microservices (Kafka)

Two independent Spring Boot services communicating asynchronously through Apache Kafka.

    POST /api/orders -> order-service (8081) -> topic "order-events" -> notification-service (8082)
                                                                          |-> 3 retries -> "order-events.DLT"
                                                                          `-> GET /api/notifications

## Run
1. `docker compose up -d`  (Kafka on localhost:9092, Kafka UI on http://localhost:8080)
2. Start `OrderServiceApplication` and `NotificationServiceApplication` in IntelliJ (or `mvn spring-boot:run` in each folder). Needs Java 17+.
3. Create an order:

       curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d "{\"customerName\":\"Asha\",\"customerEmail\":\"asha@example.com\",\"product\":\"Laptop\",\"quantity\":2,\"price\":50000}"

4. Check: `curl http://localhost:8082/api/notifications`

## Test the resilience
- **Restart safety:** stop notification-service, create 3 orders, start it again -> all 3 get processed.
- **Retry + DLT:** use an email containing `fail` (e.g. fail@example.com) -> 3 retries, then it lands in `order-events.DLT`.
- **Idempotency:** a duplicate event for the same orderId is ignored.
