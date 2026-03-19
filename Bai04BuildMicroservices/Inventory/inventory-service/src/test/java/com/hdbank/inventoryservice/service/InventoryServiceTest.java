package com.hdbank.inventoryservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.hdbank.inventoryservice.dto.InventoryResponse;
import com.hdbank.inventoryservice.model.Inventory;
import com.hdbank.inventoryservice.repository.InventoryRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void checkStock_returnsFalseForMissingSku() {
        when(inventoryRepository.findBySkuCodeIn(List.of("iphone_13", "missing")))
                .thenReturn(List.of(
                        Inventory.builder().skuCode("iphone_13").quantity(10).build()
                ));

        List<InventoryResponse> result = inventoryService.checkStock(List.of("iphone_13", "missing"));

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSkuCode()).isEqualTo("iphone_13");
        assertThat(result.get(0).isInStock()).isTrue();
        assertThat(result.get(1).getSkuCode()).isEqualTo("missing");
        assertThat(result.get(1).isInStock()).isFalse();
    }

    @Test
    void checkStock_emptyList_returnsEmpty() {
        List<InventoryResponse> result = inventoryService.checkStock(List.of());
        assertThat(result).isEmpty();
    }

    @Test
    void checkStock_zeroQuantity_returnsFalse() {
        when(inventoryRepository.findBySkuCodeIn(List.of("iphone_13_red")))
                .thenReturn(List.of(
                        Inventory.builder().skuCode("iphone_13_red").quantity(0).build()
                ));

        List<InventoryResponse> result = inventoryService.checkStock(List.of("iphone_13_red"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isInStock()).isFalse();
    }
}
