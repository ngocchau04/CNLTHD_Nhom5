package com.hdbank.order_service.IntegrationTest;

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
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Lưu ý: Mình đã loại bỏ orderService và orderRepository vì chúng chưa được sử dụng 
     * để stubbing (kịch bản giả lập) trong hàm test này. 
     * Nếu sau này Châu cần viết các kịch bản kiểm tra sâu hơn (ví dụ verify service được gọi), 
     * Châu có thể thêm lại chúng với @MockitoBean.
     */

    @Test
    void shouldCreateOrder() throws Exception {
        // Nội dung request gửi đi
        String orderRequestString = "{\"skuCode\": \"iphone_13\", \"price\": 1200, \"quantity\": 1}";

        mockMvc.perform(post("/api/order")
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON)) 
                .content(Objects.requireNonNull(orderRequestString))) 
                .andExpect(status().isCreated());
    }
}