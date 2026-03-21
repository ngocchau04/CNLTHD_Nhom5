# Product Service — Báo cáo

Đây là mô-đun `product-service` (microservice) trong bài tập Build Microservices. Tập trung cung cấp API quản lý sản phẩm (CRUD cơ bản: tạo và lấy danh sách).

## Mục đích
- Cung cấp REST API cho quản lý sản phẩm.
- Lưu trữ dữ liệu bằng MongoDB (Spring Data MongoDB).
- Expose health/metrics (Actuator + Micrometer Prometheus).

## Cấu trúc chính (package)
- `com.hdbank.productservice` - điểm khởi đầu ứng dụng (`ProductServiceApplication`).
- `controller` - `ProductController` (endpoints REST).
- `service` - `ProductService` (logic nghiệp vụ).
- `repository` - `ProductRepository` (Spring Data MongoDB repository interface).
- `model` - `Product` (document MongoDB).
- `dto` - `ProductRequest`, `ProductResponse` (request/response payloads).

## Các endpoint
- POST /api/products
  - Mô tả: tạo sản phẩm mới
  - Body: JSON { "name": "...", "description": "...", "price": 123.45 }
  - Trả về: 201 Created (không trả body)

- GET /api/products
  - Mô tả: lấy danh sách sản phẩm
  - Trả về: List of ProductResponse (id, name, description, price)

## Mô hình dữ liệu (tóm tắt)
- Product
  - id: String
  - name: String
  - description: String
  - price: BigDecimal

## Build / Run
Yêu cầu: JDK 17, Maven.

PowerShell:
- Build và chạy test:
  cd "<project>/product-service"; mvn -U clean package
- Chạy ứng dụng:
  cd "<project>/product-service"; mvn spring-boot:run
- Hoặc chạy jar:
  java -jar target/product-service-0.0.1-SNAPSHOT.jar

Sau khi chạy, service lắng nghe cổng mặc định 8080 (có thể thay đổi trong application.properties nếu cần).

## Dependencies chính
- Spring Boot Starter Web
- Spring Boot Starter Data MongoDB
- Spring Boot Actuator
- Micrometer Prometheus
- (Dev/Test) Spring Boot Starter Test
- Lombok: được quản lý trong pom — một số file đã được thay thế bằng mã thủ công để tránh phụ thuộc vào annotation processing trong IDE.

## Kiểm thử
- Test đơn vị/môi trường: `mvn test`.
- Có sẵn test khởi động ứng dụng (`ProductServiceApplicationTests`).

## Vấn đề đã gặp & Khuyến nghị
- Một số IDE (ví dụ NetBeans/nbjavac) gặp lỗi Lombok annotation-processor (NoClassDefFoundError cho `lombok.javac.Javac`) dẫn tới cảnh báo/ lỗi trong trình soạn thảo dù Maven build có thể thành công.
  - Giải pháp:
    - Chạy build bằng Maven (xác nhận): `mvn -U clean package`.
    - Đảm bảo IDE sử dụng JDK 17 và bật annotation processing / cài plugin Lombok.
    - Nếu cần tránh cảnh báo IDE tạm thời, một số lớp đã được chuyển từ Lombok sang constructor/getter/setter thủ công.

## Ghi chú cho người phát triển
- Nếu pom.xml thay đổi: re-import project Maven trong IDE hoặc restart Java language server.
- Đặt `JAVA_HOME` trỏ tới JDK 17 khi build.