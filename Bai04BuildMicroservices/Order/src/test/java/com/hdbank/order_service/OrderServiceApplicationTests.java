package com.hdbank.order_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdbank.order_service.dto.OrderLineItemsDto;
import com.hdbank.order_service.dto.OrderRequest;
import com.hdbank.order_service.repository.OrderRepository;
import com.hdbank.order_service.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testPlaceOrderSuccess() throws Exception {
        OrderLineItemsDto item = new OrderLineItemsDto("iphone_13", BigDecimal.valueOf(1200), 1);
        OrderRequest request = new OrderRequest(List.of(item));

        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(null);

        // 1. Gửi request và bảo MockMvc đợi tiến trình ngầm (async) khởi động
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(request().asyncStarted())
                .andReturn();

        // 2. Nhận kết quả SAU KHI tiến trình ngầm chạy xong
        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order Placed"));
    }

    @Test
    void testPlaceOrderFailureFallback() throws Exception {
        OrderLineItemsDto item = new OrderLineItemsDto("macbook_pro", BigDecimal.valueOf(2000), 1);
        OrderRequest request = new OrderRequest(List.of(item));

        Throwable simulatedError = new RuntimeException("Inventory Service is down");

        CompletableFuture<String> result = orderService.fallbackMethod(request, simulatedError);

        Assertions.assertTrue(result.get().contains("Oops! Lỗi khi kết nối"));
    }
}