package com.hdbank.order_service.controller;

import com.hdbank.order_service.dto.OrderRequest;
import com.hdbank.order_service.dto.OrderResponse;
import com.hdbank.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 1. Tạo đơn hàng mới
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Trả về HTTP Status 201
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        // Chuyển Request xuống Service xử lý và trả về kết quả
        return orderService.placeOrder(orderRequest);
    }

    // 2. Lấy danh sách tất cả đơn hàng
    @GetMapping
    @ResponseStatus(HttpStatus.OK) // Trả về HTTP Status 200
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 3. Lấy thông tin 1 đơn hàng cụ thể theo ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // 4. Cập nhật đơn hàng (Thay đổi giỏ hàng)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(id, orderRequest);
    }

    // 5. Xóa đơn hàng
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}