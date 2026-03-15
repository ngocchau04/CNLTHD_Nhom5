package com.hdbank.order_service.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    // Đã sửa lại thành chữ I hoa: orderLineItemsDtoList
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}