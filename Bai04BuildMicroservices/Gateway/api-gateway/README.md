🚀 Báo Cáo & Hướng Dẫn Chạy Demo API Gateway

1. Tổng Quan

Dự án này là API Gateway, đóng vai trò là "cửa ngõ" duy nhất (Single Entry Point) cho toàn bộ hệ thống Microservices. Mọi yêu cầu (request) từ người dùng (Client/Postman) đều sẽ đi qua Gateway này trước khi được định tuyến (routing) đến các dịch vụ phía sau (như Product Service, Order Service,...).

2. Các Công Việc Đã Hoàn Thành

Trong quá trình phát triển API Gateway, các công việc sau đã được thực hiện và kiểm thử thành công:

Cấu hình Định tuyến (Routing): Thiết lập thành công cơ chế chuyển hướng Request. Để phục vụ việc test độc lập, Gateway hiện được cấu hình định tuyến tạm thời tới jsonplaceholder. Các cấu hình định tuyến thông qua Eureka (dùng lb://) đã được chuẩn bị sẵn trong file application.properties và được chú thích lại để dễ dàng kích hoạt khi ráp nối toàn hệ thống.

Vô hiệu hóa Bảo mật để Test (Security Bypass): Tùy chỉnh GatewayConfig.java để tắt CSRF và cho phép mọi request đi qua (permitAll()), tránh lỗi 401 Unauthorized trong quá trình phát triển ban đầu.

Tích hợp Giám sát (Monitoring): Cấu hình mở các endpoint của Spring Boot Actuator và Prometheus (như /actuator/health và /actuator/prometheus) để chuẩn bị cho việc giám sát sức khỏe hệ thống.

Triển khai Docker (Containerization): - Tạo Dockerfile tối ưu sử dụng môi trường eclipse-temurin:17-jre-alpine để chạy file .jar.

Thiết lập docker-compose.yml cho phép build và khởi chạy Gateway hoàn toàn độc lập chỉ với một câu lệnh.

3. Hướng Dẫn Chạy Demo (Chạy Độc Lập)

Phần này hướng dẫn cách chạy trực tiếp API Gateway thông qua Docker mà không cần bật các Service khác (như Product hay Eureka).

Yêu Cầu Cần Có:

Đã cài đặt và khởi động Docker Desktop.

Mã nguồn đã được biên dịch thành file .jar.

Các Bước Thực Hiện:

Bước 1: Build mã nguồn thành file .jar
Mở Terminal tại thư mục gốc của api-gateway và chạy lệnh sau để Maven đóng gói ứng dụng:

./mvnw clean package -DskipTests


(Hoặc dùng mvn clean package -DskipTests nếu máy đã cài sẵn Maven).

Bước 2: Khởi động hệ thống với Docker Compose
Đảm bảo bạn vẫn đang ở trong thư mục api-gateway (nơi chứa file docker-compose.yml), chạy lệnh:

docker-compose up --build -d


Lệnh này sẽ tạo ra một container tên là apigateway-service và mở cổng 8080.

Bước 3: Kiểm tra (Test) bằng Postman

Mở ứng dụng Postman.

Chọn Method: GET

Nhập URL: http://localhost:8080/posts/1

Bấm Send.

Kết Quả Mong Đợi:
Hệ thống trả về HTTP Status 200 OK kèm theo đoạn dữ liệu JSON (được Gateway tự động fetch về từ trang web giả lập). Điều này chứng minh Gateway đã hoạt động ổn định và định tuyến chính xác.

4. Kế Hoạch Tích Hợp (Next Steps)

Để ráp nối API Gateway vào hệ thống Microservices hoàn chỉnh, chỉ cần thực hiện các thao tác nhỏ sau:

Bật Discovery Server (Eureka) và Product Service.

Mở file application.properties của Gateway, xóa các dấu # để bật lại cấu hình kết nối Eureka.

Đổi định tuyến của Product về lại lb://product-service.

Chạy lại Docker Compose.