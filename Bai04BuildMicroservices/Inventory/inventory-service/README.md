# Inventory Service

## Yêu cầu
- Java 17
- Maven (hoặc dùng mvnw)
- Docker Desktop (nếu chạy bằng Docker Compose)

## Chạy bằng Docker Compose
`powershell
docker compose up -d --build
`

Kiểm tra dịch vụ:
- Eureka: http://localhost:8761
- Inventory API:
  - http://localhost:8080/api/inventory?skuCode=iphone_13&skuCode=iphone_13_red
- Prometheus: http://localhost:9090
- Zipkin: http://localhost:9411

Tắt dịch vụ:
`powershell
docker compose down
`

## Chạy local (không Docker)
1. Bật MySQL và tạo database inventory_service.
2. Cập nhật src/main/resources/application.properties nếu cần.
3. Chạy:
`powershell
./mvnw spring-boot:run
`

## Chạy test
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

Dữ liệu mẫu:
- iphone_13 (quantity 10)
- iphone_13_red (quantity 0)
