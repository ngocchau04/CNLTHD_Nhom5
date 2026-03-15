package com.hdbank.order_service.service;

import com.hdbank.order_service.dto.OrderLineItemsDto;
import com.hdbank.order_service.dto.OrderRequest;
import com.hdbank.order_service.model.Order;
import com.hdbank.order_service.model.OrderLineItems;
import com.hdbank.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Áp dụng Resilience4j: Circuit Breaker, Retry và TimeLimiter theo đúng yêu cầu
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        // CHÚ Ý: Đã sửa thành getOrderLineItemsDtoList() (chữ I viết hoa) để khớp với JSON
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        return CompletableFuture.supplyAsync(() -> {
            // Giả lập gọi Inventory Service (luôn trả về true - có hàng)
            boolean allProductsInStock = true; 

            if (allProductsInStock) {
                // 1. Lưu đơn hàng vào cơ sở dữ liệu MySQL
                orderRepository.save(order);
                
                // 2. Gửi thông báo đến Kafka (Đã mở lại code để chạy full chức năng)
                kafkaTemplate.send("notificationTopic", "Đơn hàng mới đã được đặt: " + order.getOrderNumber());
                
                return "Order Placed";
            } else {
                throw new IllegalArgumentException("Sản phẩm không có trong kho, vui lòng thử lại sau.");
            }
        });
    }

    // Xử lý lỗi: Trả về thông báo lỗi khi Inventory Service gặp sự cố hoặc Timeout
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, Throwable throwable) {
        return CompletableFuture.supplyAsync(() -> "Oops! Lỗi khi kết nối (có thể do Kafka chưa lên hẳn). Vui lòng thử lại sau! Chi tiết: " + throwable.getMessage());
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}