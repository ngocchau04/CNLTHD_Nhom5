# ProjectWeb – Hướng dẫn chạy (Windows)

Tài liệu này hướng dẫn chạy **đầy đủ 1 web** gồm Backend (microservices + API Gateway) và Frontend.

## 1. Yêu cầu
- **JDK 21** (Java 21)
- **Maven**
- **Docker Desktop**
- **Node.js 18+** (khuyên dùng 20)
- Windows PowerShell

## 2. Cấu trúc
- `ProjectWeb-main/BE/` : Backend microservices
- `ProjectWeb-main/FE/react-e-commerce/` : Frontend React (Vite)
- `project_backend.htm`, `project_microservice.htm` : tài liệu (không cần chạy)

## 3. Chạy Backend (Docker)
### 3.1 Build JAR
```powershell
cd d:\Bai_6\ProjectWeb-main\BE
mvn -DskipTests clean package
```

### 3.2 Build image Eureka
```powershell
cd d:\Bai_6\ProjectWeb-main\BE\eureka-server
docker build -t eureka-server .
```

### 3.3 Chạy toàn bộ backend
```powershell
cd d:\Bai_6\ProjectWeb-main\BE
docker compose up -d --build
```

### 3.4 Kiểm tra
- API Gateway: `http://localhost:8181`
- Eureka: `http://localhost:8761`
- Kafka UI: `http://localhost:8080`

> Lưu ý: Trang `http://localhost:8181` trả về **404** là bình thường vì gateway không có route `/`.

## 4. Chạy Frontend (khuyến nghị chạy bằng npm)
Do lỗi Rollup trong Docker, frontend nên chạy trực tiếp:
```powershell
cd d:\Bai_6\ProjectWeb-main\FE\react-e-commerce
npm install
npm run dev
```

Mở trình duyệt: `http://localhost:5173`

## 5. Dữ liệu sản phẩm (seed tự động)
Backend mặc định **tự seed 5 sản phẩm mẫu** khi database còn trống.
Nếu đã có dữ liệu thì seed sẽ **không ghi đè**.

Seed hiện dùng **ảnh Google Drive dạng thumbnail**.
Link mẫu dạng:
```
https://drive.google.com/thumbnail?id=FILE_ID&sz=w800
```

Muốn seed chạy lại (khi DB đã có dữ liệu), cần xoá dữ liệu cũ trong MongoDB trước, sau đó restart `product-service`.

Nếu bạn muốn tự thêm sản phẩm theo ý mình, dùng các lệnh dưới đây:

### 5.1 Thêm 1 sản phẩm mẫu
```powershell
$body = @{
  name="Headphone A"
  price=1990000
  description="Demo"
  image="https://picsum.photos/400/300?1"
  rating=5
  quantity=20
  productCode="HP-A"
} | ConvertTo-Json -Compress

Invoke-RestMethod -Method Post -Uri "http://localhost:8082/v1/api/products" -ContentType "application/json" -Body $body
```

### 5.2 Thêm 5 sản phẩm mẫu
```powershell
$items = @(
  @{name="Headphone B"; price=1490000; image="https://picsum.photos/400/300?2"; code="HP-B"},
  @{name="Headphone C"; price=990000;  image="https://picsum.photos/400/300?3"; code="HP-C"},
  @{name="Headphone D"; price=2590000; image="https://picsum.photos/400/300?4"; code="HP-D"},
  @{name="Headphone E"; price=1790000; image="https://picsum.photos/400/300?5"; code="HP-E"},
  @{name="Headphone F"; price=2190000; image="https://picsum.photos/400/300?6"; code="HP-F"}
)

foreach ($i in $items) {
  $body = @{
    name=$i.name; price=$i.price; description="Demo"
    image=$i.image; rating=5; quantity=20; productCode=$i.code
  } | ConvertTo-Json -Compress

  Invoke-RestMethod -Method Post -Uri "http://localhost:8082/v1/api/products" -ContentType "application/json" -Body $body
}
```

## 6. Đổi ảnh sản phẩm theo `productCode`
Đã bổ sung API cập nhật ảnh:
```
PUT /v1/api/products/{code}/image?url=...
```
Ví dụ:
```powershell
curl.exe -X PUT "http://localhost:8181/v1/api/products/HP-A/image?url=https://your-image-url"
```

Nếu dùng ảnh Google Drive, nên dùng link dạng:
```
https://drive.google.com/thumbnail?id=FILE_ID&sz=w800
```

## 7. Dừng hệ thống
```powershell
cd d:\Bai_6\ProjectWeb-main\BE
docker compose down
```

Frontend (npm): nhấn `Ctrl + C` để dừng.

---
