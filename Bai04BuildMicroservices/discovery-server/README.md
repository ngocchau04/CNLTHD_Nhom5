Discovery Service

Mục đích

Discovery Service dùng để quản lý và theo dõi các service trong hệ thống microservices. Các service khi chạy sẽ đăng ký vào đây để các service khác có thể tìm và gọi lẫn nhau.

Công nghệ sử dụng

- Spring Boot
- Eureka Server
- Actuator

Cấu trúc dự án

discovery-server/

 ├── src/
 
 ├── pom.xml
 
 ├── mvnw
 
 ├── mvnw.cmd

Cấu hình chính

File application.properties:

spring.application.name=discovery-server
server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

Chạy ứng dụng

Tại thư mục discovery-server, chạy lệnh:

mvn spring-boot:run

Truy cập hệ thống

http://localhost:8761

Kiểm tra trạng thái hệ thống

http://localhost:8761/actuator/health

Kết quả trả về trạng thái hoạt động của service.

Ghi chú

- Discovery Service không tự đăng ký vào hệ thống
- Danh sách service sẽ trống nếu chưa có service nào kết nối
- Các service khác cần cấu hình địa chỉ để đăng ký vào Discovery Service

Kết luận

Discovery Service đã được xây dựng và chạy thành công. Hệ thống sẵn sàng để các service khác kết nối và đăng ký.
