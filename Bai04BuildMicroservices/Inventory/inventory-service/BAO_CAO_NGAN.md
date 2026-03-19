# Báo cáo ngắn – Inventory Service

## Thông tin chung
- Tên project: Inventory Service
- Ngôn ngữ: Java 17
- Framework: Spring Boot
- CSDL: MySQL
- Kiến trúc: Microservice (Eureka Discovery)

## Chức năng đã thực hiện
- Xây dựng REST API kiểm tra tồn kho: GET /api/inventory?skuCode=...
- Lưu trữ dữ liệu bằng Spring Data JPA
- Đăng ký service với Eureka Server
- Tích hợp Actuator, Prometheus, Zipkin
- Viết unit test và integration test
- Docker hóa ứng dụng + Docker Compose chạy cùng MySQL và Eureka
- Seed dữ liệu tự động khi khởi động

## API chính
- GET /api/inventory?skuCode=iphone_13&skuCode=iphone_13_red

Ví dụ response:
`json
[
  {"skuCode":"iphone_13","isInStock":true},
  {"skuCode":"iphone_13_red","isInStock":false}
]
`

## Cấu trúc dự án
- src/main/java/com/hdbank/inventoryservice/controller
- src/main/java/com/hdbank/inventoryservice/service
- src/main/java/com/hdbank/inventoryservice/repository
- src/main/java/com/hdbank/inventoryservice/model
- src/main/java/com/hdbank/inventoryservice/dto
- src/main/resources/application.properties

## Hướng dẫn chạy nhanh
### Docker Compose
`powershell
docker compose up -d --build
`

### Truy cập
- Eureka: http://localhost:8761
- Inventory API: http://localhost:8080/api/inventory?skuCode=iphone_13&skuCode=iphone_13_red
- Prometheus: http://localhost:9090
- Zipkin: http://localhost:9411

## Test & Coverage
Chạy test:
`powershell
./mvnw test
`

Báo cáo coverage:
`
target/site/jacoco/index.html
`

## Seed dữ liệu tự động
- MySQL: src/main/resources/data-mysql.sql
- Test (H2): src/test/resources/data-h2.sql

## Kết luận
Đã hoàn thành đầy đủ các yêu cầu bắt buộc của đề bài. Phần mở rộng chưa thực hiện.
