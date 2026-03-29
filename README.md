# CNLTHD_Nhom5


## Thành viên nhóm 5:
1. 3122411020 - Đàm Thị Ngọc Châu (Nhóm trưởng)
2. 3122411049 - Lê Gia Hân
3. 3122411173 - Võ Hoàng Kim Quyên
4. 3122411243 - Phan Thị Hải Vân
5. 3122411141 - Phan Thị Hồng Nhiên


Hướng dẫn chạy dự án:

---

BƯỚC 1: CHẠY DATABASE

Mở terminal, chạy:

docker run -d -p 27017:27017 --name mongodb mongo

Nếu báo lỗi port thì bỏ qua bước này (có thể máy đã có MongoDB rồi)

---

BƯỚC 2: CHẠY BACKEND

2.1 Vào đúng thư mục

cd BE/product-service

2.2 Build (bỏ qua test cho đỡ lỗi)

mvn clean install -DskipTests

2.3 Chạy server

mvn spring-boot:run

---

2.4 Kiểm tra backend đã chạy chưa

Mở trình duyệt:

http://localhost:8181/product-service/v1/api/products

Nếu thấy:

- có chữ JSON (dù rỗng cũng được)
  → là backend OK

---

BƯỚC 3: CHẠY FRONTEND

Mở terminal mới:

cd FE
npm install
npm run dev

---

BƯỚC 4: MỞ GIAO DIỆN

Mở trình duyệt:

http://localhost:5173

---

BƯỚC 5: NẾU KHÔNG THẤY SẢN PHẨM

→ do chưa có dữ liệu

Mở Postman hoặc dùng file test, gửi:

POST http://localhost:8181/product-service/v1/api/products

Body:

{
  "name": "Tai nghe",
  "price": 100,
  "quantity": 5
}

---

BƯỚC 6: LỖI THƯỜNG GẶP

 + Không chạy được backend
 
 + Sai thư mục
→ phải vào "product-service"

---

Lỗi 500 thường do:

- dữ liệu null
- mapper lỗi

→ kiểm tra terminal backend

---

Không hiện sản phẩm, do:

- chưa có dữ liệu
- FE gọi sai API

---

Các bước quan trọng nhất:

1. Chạy MongoDB
2. Chạy product-service
3. Test API
4. Chạy frontend

→ là web chạy được
