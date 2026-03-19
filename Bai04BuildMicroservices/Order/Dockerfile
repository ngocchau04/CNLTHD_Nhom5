# 1. Dùng môi trường Java 17 làm nền tảng
FROM eclipse-temurin:17-jdk-alpine

# 2. Copy file .jar từ thư mục target vào trong container và đổi tên thành app.jar
COPY target/*.jar app.jar

# 3. Mở cổng 8081 để đón khách
EXPOSE 8081

# 4. Lệnh khởi chạy ứng dụng khi container bật lên
ENTRYPOINT ["java", "-jar", "/app.jar"]