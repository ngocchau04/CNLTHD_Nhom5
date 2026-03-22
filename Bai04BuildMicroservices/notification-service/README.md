# Notification Service (Microservices)

## Mục tiêu
- Hiểu cơ chế Event-Driven Microservices.
- Sử dụng Kafka để gửi/nhận thông báo giữa các service.
- Xử lý thông báo khi đơn hàng được tạo thành công.
- Tích hợp Eureka, Actuator, Prometheus, Zipkin để giám sát.

## Công nghệ sử dụng
- Spring Boot 3.x, Java 17
- Spring Web
- Spring Kafka
- Spring Cloud Eureka Client
- Spring Boot Actuator
- Micrometer Tracing (Zipkin)
- Micrometer Registry Prometheus
- Lombok
- Docker, Docker Compose

## Cấu trúc chính
```
notification-service/
├── src/main/java/com/hdbank/notification_service
│   ├── listener/NotificationListener.java
│   ├── service/NotificationService.java
│   ├── dto/OrderNotification.java
│   └── store/NotificationStore.java
├── src/main/resources/application.properties
├── src/test/java/com/hdbank/notification_service
│   ├── NotificationServiceTest.java
│   └── NotificationListenerKafkaTest.java
├── Dockerfile
└── docker-compose.yml
```

## Chức năng đã làm
- **Kafka consumer** subscribe topic `notificationTopic`.
- **Xử lý thông báo**: ghi log + giả lập gửi email.
- **Eureka client** đăng ký service.
- **Actuator & Prometheus** expose endpoints.
- **Zipkin tracing**.
- **Test** cho xử lý message và Kafka consumer.
- **Docker** chạy cùng Kafka + Zipkin.

## Cấu hình quan trọng
Trong `application.properties`:
- `spring.application.name=notification-service`
- `eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka`
- `management.endpoints.web.exposure.include=health,prometheus`
- `management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans`
- Kafka consumer dùng `ErrorHandlingDeserializer` và `group-id=notification-service-v2` để tránh kẹt message lỗi.

## Cách chạy (đã chuẩn)

### 1) Chạy Eureka Server (local)
Eureka server được tạo ở thư mục:
```
notification-service/eureka-server
```
Chạy:
```bash
cd D:\microservice\notification-service\eureka-server
./mvnw spring-boot:run
```
Mở: `http://localhost:8761`

### 2) Chạy Kafka + Zipkin + Notification Service bằng Docker
```bash
cd D:\microservice\notification-service
docker compose up -d --build
```

### 3) Kiểm tra endpoint
- Eureka: `http://localhost:8761` (thấy `NOTIFICATION-SERVICE`)
- Health: `http://localhost:8081/actuator/health`
- Prometheus: `http://localhost:8081/actuator/prometheus`
- Zipkin: `http://localhost:9411`

> Lưu ý: dùng port `8081` do `8080` có thể bị trùng.

## Gửi message Kafka để test
```bash
docker exec -it notification-service-kafka-1 bash
kafka-console-producer --bootstrap-server kafka:9092 --topic notificationTopic
{"orderNumber":"ORD123","message":"Order Placed Successfully"}
```
Nhấn `Ctrl+C` rồi `exit`.

Xem log:
```bash
docker logs -f notification-service-notification-service-1
```
Kết quả mong đợi:
```
Notification received: orderNumber=ORD123, message=Order Placed Successfully
Simulate email: Order ORD123 -> Order Placed Successfully
```

## Chạy test
```bash
./mvnw test
```

## Ghi chú
- Đã triển khai mô hình **Event-Driven**: order-service gửi event → Kafka → notification-service nhận và xử lý.
- Notification service nhận JSON đúng format.
- Có đầy đủ monitoring: Actuator + Prometheus + Zipkin.
- Eureka đảm nhiệm Discovery.

