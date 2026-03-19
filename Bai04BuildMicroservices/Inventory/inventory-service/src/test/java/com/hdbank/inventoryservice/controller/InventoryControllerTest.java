package com.hdbank.inventoryservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.hdbank.inventoryservice.dto.InventoryResponse;
import com.hdbank.inventoryservice.service.InventoryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @Test
    void checkStock_returnsResponseList() {
        when(inventoryService.checkStock(List.of("iphone_13", "iphone_13_red")))
                .thenReturn(List.of(
                        new InventoryResponse("iphone_13", true),
                        new InventoryResponse("iphone_13_red", false)
                ));

        List<InventoryResponse> result = inventoryController.checkStock(List.of("iphone_13", "iphone_13_red"));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSkuCode()).isEqualTo("iphone_13");
        assertThat(result.get(0).isInStock()).isTrue();
        assertThat(result.get(1).getSkuCode()).isEqualTo("iphone_13_red");
        assertThat(result.get(1).isInStock()).isFalse();
    }
}
