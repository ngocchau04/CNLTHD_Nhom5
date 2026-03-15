package com.hdbank.order_service;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Lưu ý: Mình đã loại bỏ orderService và orderRepository vì chúng chưa được sử dụng 
     * trong kịch bản kiểm thử này. Nếu sau này Châu cần giả lập dữ liệu (stubbing) 
     * hoặc kiểm tra tương tác, Châu có thể thêm lại chúng với @MockitoBean.
     */

    @Test
    void shouldCreateOrder() throws Exception {
        // Nội dung request JSON mẫu
        String orderRequestString = "{\"skuCode\": \"iphone_13\", \"price\": 1200, \"quantity\": 1}";

        mockMvc.perform(post("/api/order")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON)) 
                .content(Objects.requireNonNull(orderRequestString))) 
                .andExpect(status().isCreated());
    }
}