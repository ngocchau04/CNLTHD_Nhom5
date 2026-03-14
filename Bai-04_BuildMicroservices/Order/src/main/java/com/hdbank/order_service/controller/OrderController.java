package com.hdbank.order_service.controller;

import com.hdbank.order_service.dto.OrderRequest;
import com.hdbank.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Trả về HTTP Status 201
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        // Chuyển Request xuống Service xử lý và trả về kết quả
        return orderService.placeOrder(orderRequest);
    }
}