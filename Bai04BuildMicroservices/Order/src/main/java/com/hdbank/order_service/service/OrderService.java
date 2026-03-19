package com.hdbank.order_service.service;

import com.hdbank.order_service.dto.OrderLineItemsDto;
import com.hdbank.order_service.dto.OrderRequest;
import com.hdbank.order_service.dto.OrderResponse;
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

    // Hàm lấy danh sách tất cả đơn hàng
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    // Hàm phụ trợ để chuyển đổi từ Entity Order sang DTO OrderResponse
    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderLineItemsDto> orderLineItemsDtoList = order.getOrderLineItemsList()
                .stream()
                .map(item -> new OrderLineItemsDto(item.getSkuCode(), item.getPrice(), item.getQuantity()))
                .toList();
        
        return new OrderResponse(order.getId(), order.getOrderNumber(), orderLineItemsDtoList);
    }

    // 1. Lấy thông tin 1 đơn hàng cụ thể theo ID
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
        return mapToOrderResponse(order);
    }

    // 2. Cập nhật đơn hàng (Thay đổi giỏ hàng)
    public String updateOrder(Long id, OrderRequest orderRequest) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));

        // Xóa danh sách cũ và cập nhật danh sách sản phẩm mới
        List<OrderLineItems> updatedItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(dto -> {
                    OrderLineItems item = new OrderLineItems();
                    item.setSkuCode(dto.getSkuCode());
                    item.setPrice(dto.getPrice());
                    item.setQuantity(dto.getQuantity());
                    return item;
                }).toList();

        existingOrder.setOrderLineItemsList(updatedItems);
        orderRepository.save(existingOrder);
        
        return "Order Updated (Cập nhật thành công)";
    }

    // 3. Xóa đơn hàng
    public String deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + id);
        }
        orderRepository.deleteById(id);
        
        return "Order Deleted (Xóa thành công)";
    }
}