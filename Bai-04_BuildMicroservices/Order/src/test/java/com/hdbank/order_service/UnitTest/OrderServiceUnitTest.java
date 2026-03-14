package com.hdbank.order_service.UnitTest;

import com.hdbank.order_service.dto.OrderLineItemsDto;
import com.hdbank.order_service.dto.OrderRequest;
import com.hdbank.order_service.repository.OrderRepository;
import com.hdbank.order_service.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    // Bơm các Mock giả vào OrderService
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Unit Test: Fallback method trả về thông báo lỗi khi Inventory Service sập")
    void testPlaceOrderOutOfStock_Fallback() throws Exception {
        // Chuẩn bị dữ liệu
        OrderLineItemsDto item = new OrderLineItemsDto("macbook_pro", BigDecimal.valueOf(2000), 1);
        OrderRequest request = new OrderRequest(List.of(item));

        // Lỗi giả lập báo hết hàng
        Throwable simulatedError = new IllegalArgumentException("Sản phẩm không có trong kho");

        // Gọi thẳng hàm bên trong Service
        CompletableFuture<String> result = orderService.fallbackMethod(request, simulatedError);

        // Kiểm tra kết quả trả về
        Assertions.assertTrue(result.get().contains("Oops! Lỗi khi kết nối"));
    }
}